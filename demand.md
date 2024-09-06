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

## 1. 验证码：$preifx/captcha

### 请求类型：get请求

### 接口备注

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

### 请求类型：post请求

### 接口备注

传入明文账号密码以及验证码答案。

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
    "message": "success"
}
```

## 3. 登录API：$preifx/login

### 请求类型：post请求


### 接口备注

传入明文账号密码以及验证码，返回token

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
    "data": "token_example"
}
```

## 4. 更新个人信息: $preifx/set_info

### 请求类型：post请求

### 接口备注

参数中一定要有id这一项，id一定要和token匹配，否则无法正常修改

其余字段可以随意增减

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

### 返回值

```json
{
    "code": 3040,
    "message": "success"
}
```

## 5. 获取个人信息：$preifx/get_info

### 请求类型：get请求

### 接口备注

获取用户信息

### 参数

无

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

## 6. 根据用户名查询用户的id: $prefix/get_id

### 请求类型：post请求

### 接口备注

传入用户的名字查询他的id

### 参数

```json
{
    "username": "Alice"
}

```

### 返回值

data里直接放用户的id

```json 
{
    "code": 3040,
    "data": 2
}
```

# 二、任务相关

$prefix = /api/task

## 1. 地图界面：请求任务（在切换到该页面时）$preifx/task

# 三、功能相关

$prefix = /api/func

## 1. 积分排名：$preifx/rank

### 请求类型：get请求

### 接口备注

返回积分排名的信息（一个数组），每个元素包含rank, user_id, username, point

### 参数

无

### 返回值

```json
{
    "code":3040,
    "data": [
        {
            "rank": 1,
            "user_id": 3,
            "username": "Tom",
            "point": 100,
        },
        {
            "rank": 2,
            "user_id": 4,
            "username": "Alice",
            "point": 90,
        },
        {
            "rank": 2,
            "user_id": 5,
            "username": "Bob",
            "point": 90,
        },
        {
            "rank": 4,
            "user_id": 6,
            "username": "Emily",
            "point": 80,
        },
    ]
}
```

## 2. 根据用户id查询用户trend_data: $prefix/get_user_trend

### 请求类型：post请求

### 接口备注

传入用户id，查询用户获取积分的时间和获取的积分值

### 参数

```json
{
    "user_id": 1
}
```

### 返回值

```json
{
    "code": 3040,
    "data": [
        {
            "time": "2024-08-18T10:00:00",
            "point": 0
        },
        {
            "time": "2024-08-18T14:23:02",
            "point": 50
        },
        {
            "time": "2024-08-18T15:33:36",
            "point": 100
        }
    ]
}

```

## 3. 讨论版首页：$preifx/bbs 

get 请求

### 接口备注

获取所有帖子，按最后一次回复排序，按序返回{帖子ID,发帖人id，发帖人，标题，所用积分,特殊帖}，最后一个作为保留值，后续开发可能用到。

### 参数

无

### 返回值

```json
{
    "code":3040,
    "data": [
        {
            "post_id": 1,
            "user_id": 1,
            "user": "Alice",
            "title": "Ask for help",
            "point": 50,
            "special": 0,
        },
    ],
}
```

## 4. 查看某个帖子: $prefix/bbs_get_post

### 请求类型：post 请求

### 接口备注

发送帖子的id，请求帖子的内容

### 参数

```json
{
    "post_id": 1
}

```

### 返回值

按照回帖时间列出所有该帖子的回复message

注意这里的message_id表示message在所有message中的排序，并不代表在该帖子中的顺序，可以直接忽略，发回来是为了方便（

```json 
{
    "code": 3040,
    "data": [
        {
            "message_id": 1,
            "user_id": 1,
            "username": "Alice",
            "content": "fuck me bro",
            "reply_time": "2024-09-05T15:33:48"
        },
        {
            "message_id": 3,
            "user_id": 2,
            "user_name": "Bob",
            "content": "give me wjx",
            "reply_time": "2024-09-05T15:34:48"
        }
    ],
}

```

# 四、通用工具

$prefix=/api/util

## 1. 查询当前时间戳: $prefix/now_time

### 请求类型：get 请求

### 接口备注

获取服务器当前时间

### 参数

无

### 返回值

```json
{
    "code": 3040,
    "data": "2024-09-05T15:34:48"
}
```