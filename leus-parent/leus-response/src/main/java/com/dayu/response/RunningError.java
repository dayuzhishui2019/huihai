package com.dayu.response;

public interface RunningError {

    RunningError SUCCESS = new RunningError() {
        @Override
        public String getCode() {
            return "200";
        }

        @Override
        public String getMessage() {
            return "SUCCESS";
        }
    };

    RunningError FAIL = new RunningError() {
        @Override
        public String getCode() {
            return "500";
        }

        @Override
        public String getMessage() {
            return "FAIL";
        }
    };

    RunningError STATE_CHECK_ERROR = new RunningError() {
        @Override
        public String getCode() {
            return "5001";
        }

        @Override
        public String getMessage() {
            return "运行时检查异常";
        }
    };


    String getCode();

    String getMessage();

    default RunningError message(String message) {
        RunningError thz = this;
        return new RunningError() {
            @Override
            public String getCode() {
                return thz.getCode();
            }

            @Override
            public String getMessage() {
                return message;
            }
        };
    }
}
