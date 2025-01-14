# 返回结果约定：

example:

```json
{
  "code": 0,
  "message": "success",
  "data": "aaa"
}
```

code 表示响应的状态，message 表示状态的描述，data 表示返回的数据。

0 表示成功响应，0 表示失败响应

除了注册和登录接口以外，其他的接口都请携带上 token 放在请求头里面

# 一、账号相关

$prefix = /api/user

## 1. 验证码：$preifx/captcha

### 请求类型：get 请求

### 接口备注

需要传回一个验证码图片，可以是一个算式或者字符，用 OpenCV 随便整一整生成一张图片，每张图片有对应的 token，等待用户返回结果

### 返回值

```json
{
    "code": 0,
    "data":{
        "pic_token": "aaa",
        "pic": （图片的base64)
    }
}
```

## 2. 注册 API: $preifx/signup

### 请求类型：post 请求

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
  "code": 0,
  "message": "success"
}
```

## 3. 登录 API：$preifx/login

### 请求类型：post 请求

### 接口备注

传入明文账号密码以及验证码，返回 token

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
  "code": 0,
  "data": "token_example"
}
```

## 4. 更新个人信息: $preifx/set_info

### 请求类型：post 请求

### 接口备注

参数中一定要有 id 这一项，id 一定要和 token 匹配，否则无法正常修改

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
  "code": 0,
  "message": "success"
}
```

## 5. 获取个人信息：$preifx/get_info

### 请求类型：get 请求

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

## 6. 根据用户名查询用户的 id: $prefix/get_id

### 请求类型：post 请求

### 接口备注

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
  "code": 0,
  "data": 2
}
```

## 7. 查询用户身份码: $prefix/qrcode

### 参数

传入用户的 token

```json
{
  "token": "1p1p1p2p3p4p6p7p8p9p9p9p0p"
}
```

### 返回值

返回用户的身份码

```json
{
  "code": 0,
  "data": {
    "qrCodeString": "Tsumo Nyaa!"
  }
}
```

# 二、任务相关

$prefix = /api/task

## 1. 地图界面：请求任务列表（在切换到该页面时）$preifx/get_task_list

### 请求类型：get 请求

### 接口备注

获取所有的任务包括他们的状态

### 参数

无

### 返回值

```json
{
    "code": 0,
    "message": "操作成功",
    "data": [
        {
            "taskId": 1,
            "taskTitle": "true music",
            "taskPosX": 1,
            "taskPosY": 1,
            "taskDescription": "this is true music",
            "taskDescriptionFull": null,
            "uri": "true music uri",
            "submission": false,
            "taskPoint": 50,
            "getPoint": 30,
            "taskCoin": 100,
            "hintPrice": 100,
            "taskStatus": 1,
            "hintStatus": 0, // 没有获取过
        },
        {
            "taskId": 2,
            "taskTitle": "woyao",
            "taskPosX": 2,
            "taskPosY": 2,
            "taskDescription": "wangwangsuibingbing",
            "taskDescriptionFull": null,
            "uri": "bingbing",
            "submission": false,
            "taskPoint": 30,
            "getPoint": 20,
            "taskCoin": 60,
            "hintPrice": 60,
            "taskStatus": 1,
            "hintStatus": 1, // 获取过
        }
    ]
}
```

## 2. 请求提示：给任务ID，获取提示 $prefix/hint

### 请求类型： post类型

### 参数

```json
{
  "taskId": 1,
}
```

### 返回值

```json
{
  "code": 0,
  "message": "操作成功",
  "data": "(base64)",
}
```

# 三、功能相关

$prefix = /api/func

## 1. 积分排名：$preifx/rank

### 请求类型：get 请求

### 接口备注

返回积分排名的信息（一个数组），每个元素包含 rank, user_id, username, point

### 参数

无

### 返回值

```json
{
    "code": 0,
    "message": "操作成功",
    "data": [
        {
            "rank": 1,
            "userId": 4,
            "username": "Sameyo",
            "point": 912
        },
        {
            "rank": 1,
            "userId": 5,
            "username": "Yosame",
            "point": 912
        },
        {
            "rank": 3,
            "userId": 1,
            "username": "wange",
            "point": 5
        },
        {
            "rank": 4,
            "userId": 2,
            "username": "testt",
            "point": 4
        }
    ]
}
```

## 2. 根据用户 id 查询用户 trend_data: $prefix/get_user_trend

### 请求类型：post 请求

### 接口备注

传入用户 id，查询用户积分更新的时间和更新后的积分值

### 参数

```json
{
  "user_id": 1
}
```

### 返回值

```json
{
  "code": 0,
  "message": "操作成功",
  "data": [
    {
      "userId": 5,
      "point": 0,
      "time": "2024-05-12T22:38:27"
    },
    {
      "userId": 5,
      "point": 1,
      "time": "2024-09-12T22:38:38"
    },
    {
      "userId": 5,
      "point": 5,
      "time": "2024-09-12T22:38:54"
    }
  ]
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

## 3. 通用任务答案提交：$prefix/task/submit

### 请求类型：post 请求

### 接口备注

对于提交答案的题目，往这个接口里直接传提交的 flag

### 参数

```json
{
  "task_id": 1,
  "flag": "brawl star"
}
```

### 返回值

```json
{
  "code": 0,
  "message": "",
  "data": true
}
```

## 4. 首页贴纸获取：$prefix/get_stickers

### 请求类型：get 请求

### 接口备注

返回用户拥有的stickers，是否摆放出来，摆放的位置（百分比）

### 参数

无

### 返回值

```json
{
  "code": 0,
  "message": "操作成功",
  "data": [
    {
      "stkId": 0,
      "show": true,
      "x": 5.1,
      "y": 5.233
    },
    {
      "stkId": 1,
      "show": false,
      "x": 2.0,
      "y": 3.0
    },
    {
      "stkId": 2,
      "show": true,
      "x": 0.0,
      "y": 0.0
    },
    {
      "stkId": 3,
      "show": true,
      "x": 5.1,
      "y": 5.233
    }
  ]
}
```

## 5. 首页贴纸状态更改：$prefix/modify_stickers

### 请求类型：get 请求

### 接口备注

修改一个sticker是否摆放出来，摆放的位置（百分比）

### 参数

```json
{
  "stkId": 2,
  "show": true,
  "x": 0.1,
  "y": 0.233,
}
```

### 返回值

```json
{
  "code": 0,
  "message": "",
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
  "code": 0,
  "data": "2024-09-05T15:34:48"
}
```

# 子项目

$prefix=/api/subtask

## Nim游戏

### 1. NIM 游戏初始化：$prefix/nim/init

#### 请求类型：post请求

#### 接口备注

请求开始一局nim游戏

```json
{
  "player_first": true, // 若为 false 则为人机先走
}
```

#### 返回值

返回石子堆数，游戏的token，初始的石子序列

```json
{
  "code": 0,
  "data": {
    "len": 6,
    "game_token": "A_RANDOM_TOKEN", // 唯一识别这一局游戏的 token
    "array": [1, 2, 4, 5, 1, 4] // 初始的石子个数序列
  }
}
```

### 2. NIM 游戏步骤：$prefix/nim/step

#### 请求类型：post请求

#### 接口备注

玩家进行一次游戏，传入游戏的token，以及玩家操作后的石子序列

```json
{
  "game_token": "A_RANDOM_TOKEN",
  "array": [1, 1, 4, 5, 1, 4]
}
```

#### 返回值

```json
{
  "code": 0,
  "data": {
    "status": "NOT_FINISHED",
    "array": [1, 0, 4, 5, 1, 4]
  }
}
```

```json
{
  "code": 0,
  "data": {
    "status": "FINISHED",
    "array": [0, 0, 0, 0, 0, 0],
    "winner": "player"
  }
}
```

```json
{
  "code": 0,
  "data": {
    "status": "FINISHED",
    "array": [0, 0, 0, 0, 0, 0],
    "winner": "com"
  }
}
```

## 电报员

### 1. 电报员游戏结果提交：$prefix/telegram/submit

#### 请求类型：post 请求

#### 接口备注

提交电报员游戏结果

#### 参数

```json
{
  "usernameA": "Meyosa",
  "usernameB": "Sakuria",
  "telegramId": 1,
  "score": 200,
  "monsterScore": 50,
  "time": 600,
  "hp": 1
}
```

#### 返回值

```json
{
  "code": 0,
  "message": "操作成功",
  "data": null
}
```

### 2. 电报员游戏排名list获取：$prefix/telegram/rank

#### 请求类型：get 请求

#### 接口备注

获取电报员游戏排名

#### 参数

无

#### 返回值

```json
{
    "code": 0,
    "message": "操作成功",
    "data": [
        {
            "rank": 1,
            "usernameA": "Meyosa",
            "usernameB": "Sakuria",
            "telegramId": 2,
            "score": 200,
            "monsterScore": 50,
            "time": 600,
            "hp": 1
        },
        {
            "rank": 2,
            "usernameA": "Yosame",
            "usernameB": "Sameyo",
            "telegramId": 1,
            "score": 100,
            "monsterScore": 50,
            "time": 50,
            "hp": 50
        }
    ]
}
```

### 3. 电报员游戏获取用户玩过的关卡：$prefix/telegram/submit

#### 请求类型：get 请求

#### 接口备注

通过用户的token获取用户玩过的关卡

#### 参数

无

#### 返回值

```json
{
    "code": 0,
    "message": "操作成功",
    "data": [
        1
    ]
}
```

## 时间二分

### 1.查询今日有没有提交过时间二分的答案：$prefix/binsearch/check

#### 请求类型：get 请求

#### 接口备注

通过token获取，True代表已经提交过

#### 参数

无

#### 返回值

```json
{
    "code": 0,
    "message": "操作成功",
    "data": true
}
```

### 2.提交今日答案：$prefix/binsearch/query

#### 请求类型：get 请求

#### 接口备注

提交一个请求，按（服务器？）当前时间作为今天用户query的时间，精确到分钟，返回对了(0)/早了(1)/晚了(2)，如果对了后端自动标注任务完成并加分

#### 参数

无

#### 返回值

```json
{
    "code": 0,
    "message": "操作成功",
    "data": 0
}
```

## 反向二十四点/大锅饭

前端我想的是数字两个两个地计算，为了防止用户修改数字，后端也要存当前用户还剩下什么数字？一共4关，后端还要在表中维护用户当前过了几关（如果已完全通关，仍然可以继续游玩，就返回最后一关）。

### 1.新一局游戏：$prefix/bigpot/init

#### 请求类型：get请求

#### 接口备注

返回关卡level、token、4个能保证有解的数字以及对应的哈希

#### 参数

无

#### 返回值

```json
{
    "code": 0,
    "message": "操作成功",
    "data": {
      "level": 1,
      "gameToken": "2e94997c-dc50-400c-b740-7d1ce043b6ab",
      "len": 4,
      "x": 1,
      "y": 2,
      "z": 3,
      "w": 4
    }
}
```

### 2.合并两个数字：$prefix/bigpot/cook

#### 请求类型：post请求

#### 接口备注

给两个数和操作符，后端检查用户是否真的有这两个数。操作符约定0为and，1为or，2为xor。如果数字用完且得到24，后端记录通过并返回"pass":1，否则"pass":0。用result返回运算结果（包括pass:1的情况也返回一个24）。

#### 参数

```json
{
  "game_token": "A_RANDOM_TOKEN",
  "x": 1,
  "y": 8,
  "operator": 1
}
```

#### 返回值

```json
{
  "code": 0,
  "message": "",
  "data": {
    "pass": 0,
    "result": 9,
  }
}
```

## 探店

### 1.更新各题答案：$prefix/visiting/update

#### 请求类型：post 请求

#### 接口备注

五道校园定向题，更新用户的答案（经纬度，是否在建筑内部，在建筑里面的图片需要回答几层）

#### 参数

```json
{
  "position": [
    [1.123456, 1.234567],
    [1.123456, 1.234567],
    [1.123456, 1.234567],
    [1.123456, 1.234567],
    [1.123456, 1.234567],
  ],
  "indoor": [true, true, true, true, false],
  "floor": [5, 2, 2, 4, 0],
}
```

#### 返回值

```json
{
  "code": 0,
  "message": "操作成功",
}
```

### 2.获取当前答案：$prefix/visiting/get_info

#### 请求类型：get 请求

#### 接口备注

由于前端需要显示用户当前设置的答案，所以需要一个接口从后端获取信息。返回值的data就照上一个API的参数的样子做就行了。

#### 参数

无

#### 返回值

```json
{
  "code": 0,
  "message": "操作成功",
  "data": {
    "position": [
      [1.123456, 1.234567],
      [1.123456, 1.234567],
      [1.123456, 1.234567],
      [1.123456, 1.234567],
      [1.123456, 1.234567],
    ],
    "indoor": [true, true, true, true, false],
    "floor": [5, 2, 2, 4, 0],
  }
}
```

## 日记本/菜谱

### 1.生成题目和token：$prefix/recipe/init

#### 请求类型：get 请求

#### 接口备注

请求生成5条日记本题目（从题库里面选），返回题目内容和token（不能只传ID不传token，否则可以直接把别人过题时发的请求复制过来，直接就过了）

#### 参数

无

#### 返回值

```json
{
  "code": 0,
  "message": "",
  "data": {
    "token": "recipe",
    "content": ["喵", "汪", "咩", "哞", "嘎"],
  }
}
```

### 2.回答答案：$prefix/recipe/answer

#### 请求类型：post 请求

#### 接口备注

回答上面一个请求给出的问题，只有5个标签全部答对才算通过，后端加积分，返回1代表通过（如果没过，token作废，重新获取题目）

### 参数

```json
{
  "token": "recipe",
  "answer": [1, 0, 0, 1, 1],
}
```

#### 返回值

```json
{
  "code": 0,
  "message": "",
  "data": true
}
```

## 生日蛋糕

### 1. 提交答案：$prefix/cake/submit

#### 请求类型：post请求

#### 接口备注

提交用户答案字符串，返回一个长度为9的字符串，分别表示9个格子分别是否点亮

#### 参数

```json
{
  "answer": "Yggdrasill2"
}
```

#### 返回值

```json
{
  "code": 0,
  "message": "",
  "data": "111111111"
}
```

## 卷意面

## 诗

每道题都要保存进度，防止意外关闭网页，感觉得开个新表

## 马尔科夫链

### 1.后端随机生成序列：$prefix/skittles/init

#### 请求类型：post 请求

#### 接口备注

传入按的是哪个按钮（也就是第一个声音是什么），一定要后端生成序列，不然直接就能在前端看到概率了（先把图画好想好答案，按这个概率来）

编号0~8，返回长60的序列（可以播放30s）

#### 参数

```json
{
  "start": 0
}
```

#### 返回值

```json
{
  "code": 0,
  "message": "",
  "data": [1, 2, 0, 0, 6, 8, 5, ..., 3]
  
}
```

## 蒸言

### 提交答案：$prefix/zhengyan/validate

#### 请求类型：post 请求

#### 接口备注

后端需要校验答案准确性，包括：

1. answer 数组长度是否为 9（排列非法）
2. "丨零" 等九个答案是否都出现在 answer 中（排列非法）
3. 将答案数组解析为3x3的数字矩阵，计算每一行、每一列的和是否为12（答案错误）
4. 返回正确的话，后端需要将该用户的任务状态置为已完成，并且加分
5. code为0表示数据合法，否则表示不合法
6. data为true表示已解锁，否则表示未解锁

#### 参数

```json
{
  // "answer": ["丨零", "丨一", "丨二", "丨三", "丨四", "丨五", "丨六", "丨七", "丨八"]
  "answer": [0, 1, 2, 3, 4, 5, 6, 7, 8]
}
```

#### 返回值

```json
{
  // "message": "蒸言：已解锁" // "排列非法" "蒸言：未解锁"
  "code": 0,
  "message": "蒸言：已解锁",
  "data": true
}
```
```json
{
  // "message": "蒸言：已解锁" // "排列非法" "蒸言：未解锁"
  "code": 1,
  "message": "排列非法",
  "data": false
}
```
```json
{
  // "message": "蒸言：已解锁" // "排列非法" "蒸言：未解锁"
  "code": 0,
  "message": "蒸言：未解锁",
  "data": false
}
```

## 蛋挞

### 提交答案：$prefix/tarts/w0w_f1n1sh3d

#### 请求类型：get 请求

#### 接口备注

没有备注，就是过了，加分

#### 参数

无

#### 返回值

```json
{
  "code": 0,
  "message": "操作成功",
}
```

## 校园定向

### 1.检查定位: $prefix/orientation/check

#### 请求类型：post 请求

#### 接口备注

传题目编号和定位，返回对不对，方向和误差。0代表近(50m)，1代表不远(200m)，2代表远(>200m)；0代表东，顺时针90度一档。

#### 参数

```json
{
  "level": 1,
  "lng": 121.000000,
  "lat": 31.000000,
}
```

#### 返回值

```json
{
  "code": 0,
  "message": "操作成功",
  "data": {
    "pass": false,
    "dist": 1,
    "dire": 2,
  }
}
