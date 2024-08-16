# 一、账号相关

## 1. 注册页面：/signup

需要传回一个验证码图片，可以是一个算式或者字符，用OpenCV随便整一整生成一张图片，每张图片有对应的token，等待用户返回结果

### 返回值

```json
{
    "pic_token": "aaa",
    "pic": （你看怎么方便传，base64还是其他什么）
}
```

## 2. 注册API:/api/signup

传入明文账号密码以及验证码答案，返回状态码，若验证码正确还要返回username，密码记得在后端加盐。

约定注册状态码，暂定3030注册成功，3031验证码错误，3032用户名已存在，3033用户名不符合要求，3034密码不符合要求，0其他错误请重试。

### Question

Json是否能正确处理用户名和密码带引号的场景？是否能防止SQL注入？

### 参数

```json
{
    "username": "Yosame",
    "password": "aminuosi",
    "pic_token": "aaa",
    "verify": 5
}
```

### 返回值

```json
{
    "code":3030,
    "data": {
        "username": "Yosame",
        "token": "aaa"
    }
}
```

## 3. 登录API：/api/login

传入明文账号密码，返回状态码，若登陆成功同时返回用户名（还需要返回什么后面再说）。

约定登录状态码，暂定3040登陆成功，3041登录失败，0其他错误请重试。

### Question

需要返回token并在前端记录吗？

### 参数

```json
{
    "username": "Yosame",
    "password": "aminuosi"
}
```

### 返回值

```json
{
    "code":3040,
    "data": {
        "username": "Yosame",
        "token": "aaa"
    }
}
```

## 登出API：/api/logout

约定登出状态码，暂定3049登出成功，0其他错误请重试。

### Question

是否是我传入token？

### 参数

```json
{
    "token": "aaa"
}
```

### 返回值

```json
{
    "code":3049
}
```
