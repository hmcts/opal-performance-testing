package simulations.Scripts.Headers;

/**
 * Enum representing different types of headers used in the application
 */
public enum HeaderType {
    TEST_1(1),
    TEST_2(2),    
    TEST_3(3),
    TEST_4(4),
    TEST_5(5),
    TEST_6(6),
    TEST_7(7),
    TEST_8(8),
    TEST_9(9),  
    TEST_10(10),
    TEST_11(11),
    TEST_12(12);
    
    private final int value;

    HeaderType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static HeaderType fromValue(int value) {
        for (HeaderType type : values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid header type value: " + value);
    }
}