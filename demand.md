# 返回结果约定：
 
example:

```json
{
    "code": 3040,
    "message": "success",
    "data": "aaa"
}
```

code表示响应的状态，message表示状态的描述，data表示返回的数据。

3040表示成功响应，3041表示失败响应

# 一、账号相关

$prefix = /api/user

## 1. 验证码：$preifx/captcha (get)

需要传回一个验证码图片，可以是一个算式或者字符，用OpenCV随便整一整生成一张图片，每张图片有对应的token，等待用户返回结果

### 返回值

```json
{
    "code": 3040,
    "data":{
        "pic_token": "aaa",
        "pic": （图片的base64)
    }
}
```

## 2. 注册API: $preifx/signup

传入明文账号密码以及验证码答案，返回状态码，密码记得在后端加盐。

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
    "code":3040
}
```

## 3. 登录API：$preifx/login

传入明文账号密码以及验证码，返回状态码，若登陆成功同时返回用户名（还需要返回什么后面再说）。

约定登录状态码，暂定3040登陆成功，3041登录失败，0其他错误请重试。

### Question

需要返回token并在前端记录吗？

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
    "code":3040,
    "data": "aaa"
}
```

## 4. 更新个人信息: $preifx/set_info

### 参数

```json
{
    "id": 2,
    "username": "Yosame",
    "password": "27b5dcb55789707e71fdcbd9d447dcb2",
    "nickname": "",
    "email": "",
    "userPic": "",
    "coin": 5,
    "point": 911
}
```

## 5. 获取个人信息：$preifx/get_info

### 返回值

```json
{
    "code": 0,
    "message": "操作成功",
    "data": {
        "id": 2,
        "username": "Yosame",
        "password": "27b5dcb55789707e71fdcbd9d447dcb2",
        "nickname": "",
        "email": "",
        "userPic": "",
        "createTime": "2024-09-05T13:45:52",
        "updateTime": "2024-09-05T15:33:48",
        "coin": 5,
        "point": 910
    }
}
```

# 二、任务相关

$prefix = /api/task

## 1. 地图界面：请求任务（在切换到该页面时）$preifx/task

# 三、功能相关

$prefix = /api/func

## 1. 积分排名：$preifx/rank (post token)

传入用户token，返回状态码（验证token是否有效，约定3050有效，其他无效），返回积分排名的信息（一个数组），每个元素包含rank、name和point；返回排名前十的用户的积分变化时间点（见返回值）；返回当前时间戳

### 参数

```json
{
    "token": "aaa"
}
```

### 返回值

```json
{
    "code":3040,
    "data": [
        {
            "rank": 1,
            "name": "Tom",
            "point": 100,
        },
        {
            "rank": 2,
            "name": "Alice",
            "point": 90,
        },
        {
            "rank": 2,
            "name": "Bob",
            "point": 90,
        },
        {
            "rank": 4,
            "name": "Emily",
            "point": 80,
        },
    ],
    "trendData": [
        {
            "name": "Tom",
            "data": [
                ["2024-08-18 10:00:00", 0],
                ["2024-08-18 14:23:02", 50],
                ["2024-08-18 15:33:36", 100],
            ],
        },
        {
            "name": "Alice",
            "data": [
                ["2024-08-18 10:00:00", 0],
                ["2024-08-18 16:00:05", 90],
            ],
        },
    ],
    "timeNow": "2024-08-19 01:01:02",
}
```

## 2. 讨论版首页：$preifx/bbs (post token)

传入用户token，返回状态码（验证token是否有效，约定3040有效，其他无效），所有帖子按最后一次回复排序，按序返回{帖子ID,发帖人，标题，所用积分,特殊帖}，最后一个作为保留值，后续开发可能用到。后续要点击帖子详情，访问的API是/bbs/{ID}。

### 参数

```json
{
    "token": "aaa"
}
```

### 返回值

```json
{
    "code":3050,
    "data": [
        {
            "id": 1,
            "user": "Alice",
            "title": "Ask for help",
            "point": 50,
            "special": 0,
        },
    ],
}
```