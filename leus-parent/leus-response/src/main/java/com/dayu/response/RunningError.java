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

    String getCode();

    String getMessage();

}
