package com.ind.tr.service;

import com.ind.tr.repository.MutualFundsNavDao;
import com.ind.tr.repository.MutualFundsRollingReturnsDao;
import com.ind.tr.repository.model.MutualFundNavEntity;
import com.ind.tr.repository.model.RollingReturnEntity;
import com.ind.tr.service.model.MutualFundNav;
import com.ind.tr.service.model.RollingReturn;
import com.ind.tr.service.translator.MutualFundNavTranslator;
import com.ind.tr.service.translator.RollingReturnsTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

enum Period {
    MONTH, THREE_MONTHS, SIX_MONTHS, ONE_YEAR, THREE_YEARS, FIVE_YEARS, SEVEN_YEARS, TEN_YEARS
}

@Component
public class MutualFundRollingReturnsServiceImpl implements MutualFundRollingReturnsService {

    private final Logger logger = LoggerFactory.getLogger(MutualFundRollingReturnsServiceImpl.class);
    @Autowired
    private MutualFundsNavDao mutualFundsNavDao;
    @Autowired
    private MutualFundsRollingReturnsDao rollingReturnsDao;
    @Autowired
    private MutualFundNavTranslator mutualFundNavTranslator;
    @Autowired
    private RollingReturnsTranslator rollingReturnsTranslator;

    @Override
    public void refreshMutualFundsRollingReturns() {
        long start = System.currentTimeMillis();
        refresh(mutualFundsNavDao.queryMutualFundsNavs());
        logger.debug("Meter - refresh - ms: " + (System.currentTimeMillis() - start));
    }

    @Override
    public void refreshMutualFundsRollingReturns(List<String> fundIds) {
        refresh(mutualFundsNavDao.queryMutualFundsNavs(fundIds));
    }

    private void refresh(List<MutualFundNavEntity> mutualFundNavEntityList) {

        long start = System.currentTimeMillis();
        Map<UUID, List<MutualFundNav>> navMap = mutualFundNavEntityList.stream()
                .map(mutualFundNavTranslator::fromMutualFundNavEntity)
                .collect(Collectors.groupingBy(MutualFundNav::getId));
        long end = System.currentTimeMillis();
        logger.debug("Meter - Transformation - ms: " + (end - start));

        for (Map.Entry<UUID, List<MutualFundNav>> entry : navMap.entrySet()) {
            List<RollingReturn> rollingReturns = calculateRollingReturns(entry.getValue(), entry.getKey());
            saveRollingReturns(rollingReturns);
        }
    }

    private void saveRollingReturns(List<RollingReturn> rollingReturns) {
        List<RollingReturnEntity> rollingReturnEntities = rollingReturns.stream().map(rollingReturnsTranslator::toRollingReturnEntity).toList();
        long start = System.currentTimeMillis();
        rollingReturnsDao.saveRollingReturns(rollingReturnEntities);
        logger.debug("Meter - rollingReturnsDao.saveRollingReturns - ms: " + (System.currentTimeMillis() - start));
    }

    private List<RollingReturn> calculateRollingReturns(List<MutualFundNav> navs, UUID mfId) {
        List<RollingReturn> result = new ArrayList<>();
        TreeMap<LocalDate, BigDecimal> navMap = dateToNav(navs);
        for (LocalDate date : navMap.keySet()) {
            Map<Period, BigDecimal> rollingReturnsMap = calculateRollingReturnsForADate(date, navMap.get(date), navMap);
            RollingReturn rollingReturn = new RollingReturn(
                    mfId,
                    date,
                    rollingReturnsMap.get(Period.MONTH),
                    rollingReturnsMap.get(Period.THREE_MONTHS),
                    rollingReturnsMap.get(Period.SIX_MONTHS),
                    rollingReturnsMap.get(Period.ONE_YEAR),
                    rollingReturnsMap.get(Period.THREE_YEARS),
                    rollingReturnsMap.get(Period.FIVE_YEARS),
                    rollingReturnsMap.get(Period.SEVEN_YEARS),
                    rollingReturnsMap.get(Period.TEN_YEARS));
            result.add(rollingReturn);
        }
        return result;
    }

    private TreeMap<LocalDate, BigDecimal> dateToNav(List<MutualFundNav> navs) {
        TreeMap<LocalDate, BigDecimal> result = new TreeMap<>(Comparator.reverseOrder());
        for (MutualFundNav nav : navs) {
            result.put(nav.getNav_date(), nav.getNav());
        }
        return result;
    }

    private Map<Period, BigDecimal> calculateRollingReturnsForADate(LocalDate date, BigDecimal end, TreeMap<LocalDate, BigDecimal> navMap) {
        Map<Period, BigDecimal> result = new HashMap<>();
        for (Period period : Period.values()) {
            BigDecimal start = getStartNavForPeriod(period, date, navMap);
            BigDecimal returnVal = null;
            if (start != null && start.intValue() != 0) {
                returnVal = switch (period) {
                    case MONTH, THREE_MONTHS, SIX_MONTHS, ONE_YEAR -> calculateAbsoluteReturn(start, end);
                    case THREE_YEARS -> calculateCAGR(start, end, 3);
                    case FIVE_YEARS -> calculateCAGR(start, end, 5);
                    case SEVEN_YEARS -> calculateCAGR(start, end, 7);
                    case TEN_YEARS -> calculateCAGR(start, end, 10);
                };
            }
            result.put(period, returnVal);
        }
        return result;
    }

    /**
     * Identifies date for the initial value.
     * If the date falls on Sunday/Saturday, we pick the friday that immediately precedes.
     * Returns NAV on that date.
     *
     * @param period
     * @param current
     * @param navMap
     * @return
     */
    private BigDecimal getStartNavForPeriod(Period period, LocalDate current, TreeMap<LocalDate, BigDecimal> navMap) {
        LocalDate date = switch (period) {
            case MONTH -> current.minusMonths(1);
            case THREE_MONTHS -> current.minusMonths(3);
            case SIX_MONTHS -> current.minusMonths(6);
            case ONE_YEAR -> current.minusYears(1);
            case THREE_YEARS -> current.minusYears(3);
            case FIVE_YEARS -> current.minusYears(5);
            case SEVEN_YEARS -> current.minusYears(7);
            case TEN_YEARS -> current.minusYears(10);
        };
        if (navMap.containsKey(date)) {
            return navMap.get(date);
        } else if (navMap.containsKey(date.minusDays(1))) {
            return navMap.get(date.minusDays(1));
        } else if (navMap.containsKey(date.minusDays(2))) {
            return navMap.get(date.minusDays(2));
        }
        return null;
    }

    private BigDecimal calculateCAGR(BigDecimal startValue, BigDecimal endValue, int years) {
        BigDecimal growth = endValue.divide(startValue, 5, RoundingMode.HALF_UP);
        BigDecimal cagr = BigDecimal.valueOf(Math.pow(growth.doubleValue(), 1.0 / years));
        cagr = cagr.subtract(BigDecimal.ONE).multiply(BigDecimal.valueOf(100));
        return cagr.setScale(5, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateAbsoluteReturn(BigDecimal startValue, BigDecimal endValue) {
        return ((endValue.subtract(startValue)).divide(startValue, 5, RoundingMode.HALF_UP)).multiply(BigDecimal.valueOf(100));
    }
}