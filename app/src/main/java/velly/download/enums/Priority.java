package velly.download.enums;

/**
 * @Auther: admin
 * @Date: 2019/10/8
 * @Describe: 下载优先级
 */
public enum Priority {
    low(0),
    middle(1),
    high(2);

    private Integer value;

    Priority(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public static Priority getInstance(int value){
        for (Priority priority : Priority.values()){
            if (priority.getValue() == value){
                return priority;
            }
        }

        return Priority.middle;
    }
}
