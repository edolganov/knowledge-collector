package ru.dolganov.tool.knowledge.collector.annotation;

public @interface RequestForEnhancement {
    int    id();
    String synopsis();
    String engineer() default "[unassigned]"; 
    String date()   default "[unimplemented]"; 
}
