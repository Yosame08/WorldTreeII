# 返回结果约定：

example:

```json
{
  "code": 3040,
  "message": "success",
  "data": "aaa"
}
```

code 表示响应的状态，message 表示状态的描述，data 表示返回的数据。

3040 表示成功响应，3041 表示失败响应

# 一、账号相关

$prefix = /api/user

## 1. 验证码：$preifx/captcha (get)

需要传回一个验证码图片，可以是一个算式或者字符，用 OpenCV 随便整一整生成一张图片，每张图片有对应的 token，等待用户返回结果

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

## 3. 注册 API: $preifx/signup

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
  "code": 3040,
  "message": "success"
}
```

## 3. 登录 API：$preifx/login

传入明文账号密码以及验证码，返回状态码和 token（还需要返回什么后面再说）。

约定登录状态码，暂定 3040 登陆成功，3041 登录失败。

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
  "code": 3040,
  "data": "token_example"
}
```

## 4. 更新个人信息: $preifx/set_info

### 参数

参数中一定要有 id 这一项，id 一定要和 token 匹配

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

## 6. 根据用户名查询用户的 id: $prefix/get_id

传入用户的名字查询他的 id

### 参数

```json
{
  "username": "Alice"
}
```

### 返回值

data 里直接放用户的 id

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

传入用户 token，返回状态码（验证 token 是否有效，约定 3050 有效，其他无效），返回积分排名的信息（一个数组），每个元素包含 rank,username,user_id,point

### 参数

无

### 返回值

```json
{
  "code": 3040,
  "data": [
    {
      "rank": 1,
      "user_id": 3,
      "username": "Tom",
      "point": 100
    },
    {
      "rank": 2,
      "user_id": 4,
      "username": "Alice",
      "point": 90
    },
    {
      "rank": 2,
      "user_id": 5,
      "username": "Bob",
      "point": 90
    },
    {
      "rank": 4,
      "user_id": 6,
      "username": "Emily",
      "point": 80
    }
  ]
}
```

## 2. 根据用户 id 查询用户 trend_data: $prefix/get_user_trend

传入用户 id，查询用户获取积分的时间和获取的积分值

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
    ["2024-08-18T10:00:00", 0],
    ["2024-08-18T14:23:02", 50],
    ["2024-08-18T15:33:36", 100]
  ]
}
```

## 3. 讨论版首页：$preifx/bbs (get)

获取所有帖子，按最后一次回复排序，按序返回{帖子 ID,发帖人，标题，所用积分,特殊帖}，最后一个作为保留值，后续开发可能用到。

### 参数

无

### 返回值

```json
{
  "code": 3040,
  "data": [
    {
      "id": 1,
      "user": "Alice",
      "title": "Ask for help",
      "point": 50,
      "special": 0
    }
  ]
}
```

## 4. 查看某个帖子: $prefix/bbs_get_post

### 参数

发送帖子的 id，请求帖子的内容

```json
{
  "post_id": 1
}
```

### 返回值

按照回帖时间列出所有该帖子的回复 message

注意这里的 message_id 表示 message 在所有 message 中的排序，并不代表在该帖子中的顺序，可以直接忽略，发回来是为了方便（

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
  ]
}
```

# 四、通用工具

## 1. 查询当前时间戳（这个前端有现成的不用发请求吧？

### 参数

### 返回值

# 子项目

## 1. NIM 游戏初始化：/api/nim/init

```json
{
  "player_first": true, // 若为 false 则为人机先走
  "len": 6
}
```

返回

```json
{
  "code": 0,
  "data": {
    "game_token": "A_RANDOM_TOKEN", // 唯一识别这一局游戏的 token
    "array": [1, 1, 4, 5, 1, 4] // 初始的石子个数序列
  }
}
```

## 2. NIM 游戏步骤：/api/nim/step

```json
{
  "game_token": "A_RANDOM_TOKEN",
  "take_from": 2, // 0-indexed
  "num_taken": 2
}
```

返回

```json
{
  "code": 0,
  "data": {
    "status": "NOT_FINISHED",
    "take_from": 3,
    "num_taken": 1
  }
}
```

```json
{
  "code": 1,
  "message": "Cannot take from column 2 anymore"
}
```

```json
{
  "code": 0,
  "data": {
    "status": "FINISHED",
    "winner": "player"
  }
}
```

```json
{
    "code": 0,
    "data": {
        "status": "FINISHED"
        "take_from": 3,
        "num_taken": 1,
        "winner": "com"
    }
}
```
