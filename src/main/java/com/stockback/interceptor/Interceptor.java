package com.stockback.interceptor;

import lombok.extern.log4j.Log4j2;
import org.hibernate.EmptyInterceptor;
import org.springframework.util.StringUtils;

@Log4j2
public class Interceptor extends EmptyInterceptor {

    @Override
    public String onPrepareStatement(String sql) {
        if (StringUtils.hasLength(sql) && sql.toLowerCase().startsWith("select") &&
                !sql.toLowerCase().contains("commissions_ressources")) {
//            log.info("Before: " + sql);
            if (sql.contains("nextval") || sql.contains("count"))
                return super.onPrepareStatement(sql);
            String alias = sql.split(" ")[1].split("\\.")[0];
            String entity = alias.split("0_")[0];
            if (sql.contains("where")) {
                String[] sqls = sql.split("order by");
                if (sqls.length > 1) {
                    sql = sqls[0] + "and " + alias + ".supprime=false order by" + sqls[1];
                } else {
                    sql = sql + " and " + alias + ".supprime=false";
                }
            } else {
                entity = entity.equalsIgnoreCase("user") ? "utilisateur" : entity;
                entity = entity.equalsIgnoreCase("datarefere") ? "data_reference" : entity;
                entity = entity.equalsIgnoreCase("configcmca") ? "config_cmca" : entity;
                String[] sqls = sql.split(entity + " " + alias);
                String endRequest = (sqls.length > 1) ? sqls[1] : "";
                sql = sqls[0] + entity + " " + alias + " where " + alias + ".supprime=false" + endRequest;
            }
        }
//        log.info("After: " + sql);
        return super.onPrepareStatement(sql);
    }
}
