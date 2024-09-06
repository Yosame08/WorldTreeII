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

传入明文账号密码以及验证码，返回状态码和token（还需要返回什么后面再说）。

约定登录状态码，暂定3040登陆成功，3041登录失败。

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

### 参数

参数中一定要有id这一项，id一定要和token匹配

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

## 5. 获取个人信息：$preifx/get_info(get)

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

## 1. 积分排名：$preifx/rank (get)

传入用户token，返回状态码（验证token是否有效，约定3050有效，其他无效），返回积分排名的信息（一个数组），每个元素包含rank、name和point；返回排名前十的用户的积分变化时间点（见返回值）；返回当前时间戳

### 参数

无

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

## 2. 讨论版首页：$preifx/bbs (get)

获取所有帖子，按最后一次回复排序，按序返回{帖子ID,发帖人，标题，所用积分,特殊帖}，最后一个作为保留值，后续开发可能用到。

### 参数

无

### 返回值

```json
{
    "code":3040,
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

## 3. 查看某个帖子: $prefix/bbs_get_post

### 参数

发送帖子的id，请求帖子的内容

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