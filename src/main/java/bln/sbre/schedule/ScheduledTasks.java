package bln.sbre.schedule;

import bln.sbre.client.query.*;
import bln.sbre.client.RestClient;
import bln.sbre.entity.Header;
import bln.sbre.entity.LineInterface;
import bln.sbre.entity.SubLine;
import bln.sbre.entity.enums.BatchStatusEnum;
import bln.sbre.repo.LineInterfaceRepo;
import bln.sbre.repo.PlanErrorRepo;
import bln.sbre.repo.SubLineRepo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Objects;
import static java.util.Optional.*;
import static java.util.stream.Collectors.*;
import static org.springframework.util.StringUtils.isEmpty;

@Component
@RequiredArgsConstructor
public class ScheduledTasks {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
    private final RestClient restClient;
    private final LineInterfaceRepo lineInterfaceRepo;
    private final SubLineRepo subLineRepo;
    private final PlanErrorRepo planErrorRepo;

    @Scheduled(cron = "0 * * * * *")
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void startExport() {
        List<LineInterface> lines = lineInterfaceRepo.findAllByStatus(BatchStatusEnum.W);
        List<Long> headerIds = lines.stream()
            .map(t -> t.getLine().getHeader())
            .filter(Objects::nonNull)
            .map(Header::getId)
            .distinct()
            .collect(toList());

        for (Long headerId : headerIds) {
            List<QueryRequestDto> plans = lines
                .stream()
                .filter(t -> t.getLine().getHeader().getId().equals(headerId))
                .map(this::mapLineToDto)
                .filter(Objects::nonNull)
                .collect(toList());

            if (plans.isEmpty())
                return;

            logger.info("Sending plans, headerId: {}, count of records: {}", headerId, plans.size());
            Pair<BatchStatusEnum, ResponseEntity<String>> response;
            try {
                response = send(plans);
            }
            catch (Exception e) {
                logger.error("Send plans fail", e);
                return;
            }

            if (response.getFirst() == BatchStatusEnum.C)
                handleSuccess(response, lines, plans);
            else
                handleFail(response, lines, plans);

            updateStatuses(headerId);
        }
    }

    private QueryRequestDto mapLineToDto(LineInterface li) {
        List<SubLine> hours = subLineRepo.findAllByLine(li.getLine());
        if (hours.isEmpty()) {
            logger.warn("no data hours for id: {}", li.getId());
            return null;
        }

        QueryRequestDto plan = new QueryRequestDto();
        plan.setId(li.getId());
        plan.setPlanDate(li.getPlanDate());
        plan.setOperation(li.getOperation());
        plan.setSender(new PartnerDto(li.getSender()));
        plan.setPartner(new PartnerDto(li.getPartner()));

        for (SubLine hour : hours) {
            switch (hour.getPos()) {
                case 0:
                    plan.setHour0(ofNullable(hour.getVal()).orElse(0d));
                    break;
                case 1:
                    plan.setHour1(ofNullable(hour.getVal()).orElse(0d));
                    break;
                case 2:
                    plan.setHour2(ofNullable(hour.getVal()).orElse(0d));
                    break;
                case 3:
                    plan.setHour3(ofNullable(hour.getVal()).orElse(0d));
                    break;
                case 4:
                    plan.setHour4(ofNullable(hour.getVal()).orElse(0d));
                    break;
                case 5:
                    plan.setHour5(ofNullable(hour.getVal()).orElse(0d));
                    break;
                case 6:
                    plan.setHour6(ofNullable(hour.getVal()).orElse(0d));
                    break;
                case 7:
                    plan.setHour7(ofNullable(hour.getVal()).orElse(0d));
                    break;
                case 8:
                    plan.setHour8(ofNullable(hour.getVal()).orElse(0d));
                    break;
                case 9:
                    plan.setHour9(ofNullable(hour.getVal()).orElse(0d));
                    break;
                case 10:
                    plan.setHour10(ofNullable(hour.getVal()).orElse(0d));
                    break;
                case 11:
                    plan.setHour11(ofNullable(hour.getVal()).orElse(0d));
                    break;
                case 12:
                    plan.setHour12(ofNullable(hour.getVal()).orElse(0d));
                    break;
                case 13:
                    plan.setHour13(ofNullable(hour.getVal()).orElse(0d));
                    break;
                case 14:
                    plan.setHour14(ofNullable(hour.getVal()).orElse(0d));
                    break;
                case 15:
                    plan.setHour15(ofNullable(hour.getVal()).orElse(0d));
                    break;
                case 16:
                    plan.setHour16(ofNullable(hour.getVal()).orElse(0d));
                    break;
                case 17:
                    plan.setHour17(ofNullable(hour.getVal()).orElse(0d));
                    break;
                case 18:
                    plan.setHour18(ofNullable(hour.getVal()).orElse(0d));
                    break;
                case 19:
                    plan.setHour19(ofNullable(hour.getVal()).orElse(0d));
                    break;
                case 20:
                    plan.setHour20(ofNullable(hour.getVal()).orElse(0d));
                    break;
                case 21:
                    plan.setHour21(ofNullable(hour.getVal()).orElse(0d));
                    break;
                case 22:
                    plan.setHour22(ofNullable(hour.getVal()).orElse(0d));
                    break;
                case 23:
                    plan.setHour23(ofNullable(hour.getVal()).orElse(0d));
                    break;
            }
        }

        return plan;
    }


    private Pair<BatchStatusEnum, ResponseEntity<String>> send(List<QueryRequestDto> plans) {
        BatchStatusEnum status = BatchStatusEnum.E;
        ResponseEntity<String> response;

        //sending request
        response = restClient.request(plans);
        if (response.getStatusCodeValue() < 400 || response.getStatusCodeValue() >= 500 ) {
            QueryResponseDto responseBody = restClient.jsonStringToObject(
                response.getBody(),
                QueryResponseDto.class
            );

            status = responseBody.isSuccess()
                ? BatchStatusEnum.C
                : BatchStatusEnum.E;
        }

        return Pair.of(status, response);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void updateStatuses(Long headerId) {
        try {
            lineInterfaceRepo.updateStatuses(headerId);
        }
        catch (Exception e) {
            logger.error("call procedure for update statuses fail", e);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void handleSuccess(
        Pair<BatchStatusEnum, ResponseEntity<String>> response,
        List<LineInterface> lines,
        List<QueryRequestDto> plans
    ) {
        QuerySuccessResponseDto responseSuccessBody = restClient.jsonStringToObject(
            response.getSecond().getBody(),
            QuerySuccessResponseDto.class
        );
        logger.warn("response: {}", responseSuccessBody);

        for (int i = 0; i < responseSuccessBody.getPlanIds().size(); i++) {
            Long planId = responseSuccessBody.getPlanIds().get(i);
            QueryRequestDto plan = plans.get(i);
            LineInterface line = lines.stream()
                .filter(t -> t.getId().equals(plan.getId()))
                .findFirst()
                .orElse(null);

            if (line != null) {
                line.setStatus(response.getFirst());
                line.setExternalId(planId);
                line.setError(null);
                lineInterfaceRepo.save(line);
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void handleFail(
        Pair<BatchStatusEnum, ResponseEntity<String>> response,
        List<LineInterface> lines,
        List<QueryRequestDto> plans
    ) {
        QueryFailResponseDto responseFailBody = restClient.jsonStringToObject(
            response.getSecond().getBody(),
            QueryFailResponseDto.class
        );
        logger.warn("response: {}", responseFailBody);

        Integer rowNum = getFailedRowNum(responseFailBody);
        if (rowNum == null) {
            logger.error("failed row num not found");
            return;
        }

        if (rowNum >= plans.size()) {
            logger.error("bad row num: {}", rowNum);
            return;
        }

        String errorCode = responseFailBody.getErrors()
            .stream()
            .filter(t -> !t.startsWith("ERROR_PLAN_NUMBER#"))
            .findFirst()
            .orElse(null);

        QueryRequestDto plan = plans.get(rowNum);
        LineInterface line = lines.stream()
            .filter(t -> t.getId().equals(plan.getId()))
            .findFirst()
            .orElse(null);

        if (line == null) {
            logger.error("line not found, id: {}", plan.getId());
            return;
        }

        line.setStatus(response.getFirst());
        if (!isEmpty(errorCode))
            line.setError(planErrorRepo.findOne(errorCode));
        lineInterfaceRepo.save(line);
    }

    private Integer getFailedRowNum(QueryFailResponseDto responseFailBody) {
        for (String error : responseFailBody.getErrors()) {
            if (error.startsWith("ERROR_PLAN_NUMBER#")) {
                String[] splits = error.split("#");
                String posStr = splits[1];
                if (!isEmpty(posStr))
                    return Integer.parseInt(posStr);
            }
        }

        return null;
    }
}
