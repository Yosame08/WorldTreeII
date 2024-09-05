import Mock from 'mockjs'

Mock.setup({
    timeout: '200-1500'
})

Mock.mock('/api/user/login', 'post', {
    code: 3040,
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
            code: 3041,
            message: "Captcha verification failed",
        };
    }
    if (username === 'existingUser') {
        return {
            code: 3041,
            message: "Username already exists",
        };
    }
    if (!/^[a-zA-Z0-9]{3,}$/.test(username)) {
        return {
            code: 3041,
            message: "Username must be at least 3 characters long and contain only letters and numbers",
        };
    }
    if (password.length < 6) {
        return {
            code: 3041,
            message: "Password must be at least 6 characters long",
        };
    }
    return {
        code: 3040,
        message: "",
    };
});

Mock.mock('/api/rank', 'get', () => {
    return {
        code: 3050,
        tableData: [
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
                "rank": 3,
                "name": "Bob",
                "point": 80,
            },
            {
                "rank": 3,
                "name": "Emily",
                "point": 80,
            },
        ],
        trendData: [
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
        timeNow: "2024-08-19 01:01:02",
    };
});

Mock.mock('/api/bbs', 'get', () => {
    return {
        code: 3050,
        list: [
            {
                "id": 2,
                "user": "Admin",
                "title": "New message",
                "point": 0,
                "special": 1,
            },
            {
                "id": 1,
                "user": "Alice",
                "title": "Ask for help",
                "point": 50,
                "special": 0,
            },
        ],
    };
});
