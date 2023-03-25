package com.chenpeiyu.mqtt.utils;

/*
在控制类使用，模板：
@GetMapping("/users")
    public Result<List<User>> getUsers() {
        List<User> userList = userService.getUsers();
        return Result.success(userList);
    }

    @GetMapping("/users/{id}")
    public Result<User> getUserById(@PathVariable("id") Long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return Result.fail("User not found");
        } else {
            return Result.success(user);
        }
    }
*/

public class Result<T> {

    private int code;
    private String message;
    private T data;

    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static Result<Object> success() {
        return new Result<>(200, "OK");
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(200, "OK", data);
    }

    public static Result<Object> fail(String message) {
        return new Result<>(500, message);
    }
}

