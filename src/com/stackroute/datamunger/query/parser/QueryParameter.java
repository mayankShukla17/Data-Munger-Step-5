package com.stackroute.datamunger.query.parser;

import java.util.List;

/*
 * This class will contain the elements of the parsed Query String such as conditions,
 * logical operators,aggregate functions, file name, fields group by fields, order by
 * fields, Query Type
 * */
public class QueryParameter {
    private String fileName;
    private String baseQuery;
    List<Restriction> restrictions;
    List<String> logicalOperators;
    List<String> fields;
    List<AggregateFunction> aggregateFunctions;
    List<String> groupByFields;
    List<String> orderByFields;

    public String getFileName() {
        // TODO Auto-generated method stub
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getBaseQuery() {
        // TODO Auto-generated method stub
        return baseQuery;
    }

    public void setBaseQuery(String baseQuery) {
        this.baseQuery = baseQuery;
    }

    public List<Restriction> getRestrictions() {
        // TODO Auto-generated method stub
        return restrictions;
    }

    public void setRestrictions(List<Restriction> restrictions) {
        this.restrictions = restrictions;
    }

    public List<String> getLogicalOperators() {
        // TODO Auto-generated method stub
        return logicalOperators;
    }

    public void setLogicalOperators(List<String> logicalOperators) {
        this.logicalOperators = logicalOperators;
    }

    public List<String> getFields() {
        // TODO Auto-generated method stub
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public List<AggregateFunction> getAggregateFunctions() {
        // TODO Auto-generated method stub
        return aggregateFunctions;
    }

    public void setAggregateFunctions(List<AggregateFunction> aggregateFunctions) {
        this.aggregateFunctions = aggregateFunctions;
    }

    public List<String> getGroupByFields() {
        // TODO Auto-generated method stub
        return groupByFields;
    }

    public void setGroupByFields(List<String> groupByFields) {
        this.groupByFields = groupByFields;
    }

    public List<String> getOrderByFields() {
        // TODO Auto-generated method stub
        return orderByFields;
    }

    public void setOrderByFields(List<String> orderByFields) {
        this.orderByFields = orderByFields;
    }

    public String getQUERY_TYPE() {
        // TODO Auto-generated method stub
        return null;
    }
}
