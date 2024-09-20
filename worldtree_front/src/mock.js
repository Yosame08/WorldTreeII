import Mock from 'mockjs'

Mock.setup({
    timeout: '900-1500'
})

Mock.mock('/api/user/captcha', 'get', () => {
    return {
        pic_token: Mock.Random.guid(),
        pic: Mock.Random.dataImage('80x20', 'CAPTCHA')
    };
});

Mock.mock('/api/user/signup', 'post', (options) => {
    const { username, password, pic_token, verify } = JSON.parse(options.body);

    // Mock different error codes based on input data
    if (verify === 'expectedCaptcha') {
        return {
            code: 1,
            message: "Captcha verification failed",
        };
    }
    if (username === 'existingUser') {
        return {
            code: 1,
            message: "Username already exists",
        };
    }
    if (!/^[a-zA-Z0-9]{3,}$/.test(username)) {
        return {
            code: 1,
            message: "Username must be at least 3 characters long and contain only letters and numbers",
        };
    }
    if (password.length < 6) {
        return {
            code: 1,
            message: "Password must be at least 6 characters long",
        };
    }
    return {
        code: 0,
        message: "success",
    };
});

Mock.mock('/api/user/login', 'post', {
    code: 0,
    data: 'this_is_a_token',
});

Mock.mock('/api/user/get_info', 'get', {
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
        "point": 910,
    }
});

Mock.mock('/api/user/get_id', 'post', {
    code: 0,
    data: 2,
});

Mock.mock('/api/func/rank', 'get', () => {
    return {
        code: 0,
        message: "",
        data: [
            {
                "rank": 1,
                "user_id": 1,
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
        ],
    };
});

Mock.mock('/api/func/get_user_trend', 'post', (options) => {
    let data = JSON.parse(options.body).user_id;
    console.log(data);
    if (data === 1) return {
        "code": 0,
        "message": "操作成功",
        "data": [
            {
                "userId": 0,
                "point": 0,
                "time": "2024-09-20T00:00:00"
            },
            {
                "userId": 0,
                "point": 1,
                "time": "2024-09-20T00:38:38"
            },
            {
                "userId": 0,
                "point": 5,
                "time": "2024-09-20T00:40:54"
            }
        ]
    }
    else return {
        "code": 0,
        "message": "操作成功",
        "data": [
            {
                "userId": data,
                "point": 0,
                "time": "2024-09-20T00:00:00"
            },
            {
                "userId": data,
                "point": 3,
                "time": "2024-09-20T00:44:30"
            },
            {
                "userId": data,
                "point": 10,
                "time": "2024-09-20T01:19:59"
            }
        ]
    }
});

Mock.mock('/api/task/get_task_list', 'get', () => {
    return {
        "code": 0,
        "data": [
            {
                "task_id": 1,
                "task_title": "haunted three building",
                "task_status": 0,
                "task_pos": [0.3, 0.4]
            },
            {
                "task_id": 2,
                "task_title": "haunted five building",
                "task_status": 1,
                "task_pos": [0.6, 0.7]
            }
        ]
    }
});

Mock.mock('/api/task/get_task_info', 'post', (options) => {
    return {
        "code": 0,
        "message": "操作成功",
        "data": {
            "taskId": 1,
            "taskTitle": "true music",
            "taskPosX": 1,
            "taskPosY": 1,
            "taskDescription": "[Yosame]这题我不会`但其实他还不知道，这题他会`[RPU]这题显然是个音乐题，易得1=C",
            "uri": "http://localhost:8080/tasks/bigpot",
            "submission": true,
            "taskPoint": 50,
            "getPoint": 30,
            "taskCoin": 100,
            "hintPrice": 100,
            "taskStatus": 1
        }
    }
});

Mock.mock('/api/subtask/nim/init', 'get', () => {
    return {
        code: 0,
        data: {
            "game_token": "A_RANDOM_TOKEN", // 唯一识别这一局游戏的 token
            "array": [1, 1, 4, 5, 1, 4] // 初始的石子个数序列
        }
    }
});

Mock.mock('/api/subtask/nim/step', 'post', () => {
    return {
        code: 0,
        data: {
            "status": "NOT_FINISHED",
            "array": [1, 1, 4, 5, 0, 0] // 初始的石子个数序列
        }
    }
});


Mock.mock('/api/user/qrcode', 'get', () => {
    return {
        code: 0,
        data: {
            "qrCodeString": "Tsumo nyaa!"
        }
    }
});

Mock.mock('/api/subtask/skittles/init', 'post', (options) => {
    return {
        "code": 0,
        "data": {
            "sequence": [
                JSON.parse(options.body).start, 2, 0, 0, 6, 8, 5, 3, 1, 4,
                1, 2, 0, 0, 6, 8, 5, 3, 1, 4,
                1, 2, 0, 0, 6, 8, 5, 3, 1, 4,
                1, 2, 0, 0, 6, 8, 5, 3, 1, 4,
                1, 2, 0, 0, 6, 8, 5, 3, 1, 4,
                1, 2, 0, 0, 6, 8, 5, 3, 1, 4,
            ],
        },
    }
});

Mock.mock('/api/subtask/bigpot/init', 'get', () => {
    return {
        code: 0,
        message: "",
        data: {
            level: 1,
            game_token: "A_RANDOM_TOKEN",
            array: [1, 1, 8, 16],
        }
    }
});

Mock.mock('/api/subtask/bigpot/cook', 'post', (options) => {
    // from options get (array) num and (int) operator
    let num = JSON.parse(options.body).num;
    let operator = JSON.parse(options.body).operator;
    // return the result
    let result;
    switch (operator) {
        case 0: // AND
            result = num[0] & num[1];
            break;
        case 1: // OR
            result = num[0] | num[1];
            break;
        case 2: // XOR
            result = num[0] ^ num[1];
            break;
        default:
            return res.json({ code: 1, message: 'Invalid operator' });
    }
    let p = result === 24;
    return {
        code: 0,
        message: "",
        data: {
            pass: p ? 1 : 0,
            result: result,
        }
    }
});

Mock.mock('/api/subtask/cake/submit', 'post', (options) => {
    let light = Math.random() > 0.5;
    return {
        code: 0,
        message: "",
        data: light ? "111110000" : "010101010",
    }
});

let visitingMock = {
    "position": [
        [121.500, 31.3018],
        [121.502, 31.3019],
        [121.5030, 31.3020],
        [121.5041, 31.3021],
        [121.5062, 31.3022],
    ],
    "indoor": [true, true, true, true, false],
    "floor": [5, 2, 2, 4, 0],
}

Mock.mock('/api/subtask/visiting/get_info', 'get', () => {
    return {
        "code": 0,
        "message": "操作成功",
        "data": {
            "position": visitingMock.position,
            "indoor": visitingMock.indoor,
            "floor": visitingMock.floor,
        }
    }
});

Mock.mock('/api/subtask/visiting/update', 'post', (options) => {
    let { position, indoor, floor } = JSON.parse(options.body);
    visitingMock.position = position;
    visitingMock.indoor = indoor;
    visitingMock.floor = floor;
    return {
        "code": 0,
        "message": "操作成功",
    }
});

Mock.mock('/api/func/get_stickers', 'get', () => {
    return {
        "code": 0,
        "message": "",
        "data": [
            {
                "stk_id": 1,
                "show": true,
                "x": 0.566,
                "y": 0.788,
            },
            {
                "stk_id": 4,
                "show": true,
                "x": 0.6,
                "y": 0.1,
            },
            {
                "stk_id": 8,
                "show": false,
                "x": 0.6,
                "y": 0.1,
            },
        ]
    }
});

Mock.mock('/api/func/modify_stickers', 'post', (options) => {
    console.log("Modify received: " + JSON.parse(options.body));
    return {
        "code": 0,
        "message": "",
    }
});