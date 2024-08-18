import Mock from 'mockjs'

Mock.setup({
    timeout: '400-3000'
})

Mock.mock('/api/login', 'post', {
    code: 3040,
    data: {
        username: 'TestUser'
    }
});

Mock.mock('/api/captcha', 'get', () => {
    return {
        pic_token: Mock.Random.guid(),
        pic: Mock.Random.dataImage('80x20', 'CAPTCHA')
    };
});

Mock.mock('/api/signup', 'post', (options) => {
    const { username, password, pic_token, verify } = JSON.parse(options.body);

    // Mock different error codes based on input data
    if (verify === 'expectedCaptcha') {
        return {
            code: 3031,
            data: {}
        };
    }
    if (username === 'existingUser') {
        return {
            code: 3032,
            data: {}
        };
    }
    if (!/^[a-zA-Z0-9]{3,}$/.test(username)) {
        return {
            code: 3033,
            data: {}
        };
    }
    if (password.length < 6) {
        return {
            code: 3034,
            data: {}
        };
    }
    return {
        code: 3030,
        data: {
            username: "Yosame",
            token: "aaa"
        }
    };
});

Mock.mock('/api/logout', 'post', {
    code: 200,
    data: {}
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