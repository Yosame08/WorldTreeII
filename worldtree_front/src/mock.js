import Mock from 'mockjs'

Mock.setup({
    timeout: '200-600'
})

Mock.mock('/api/login', 'post', {
    code: 3040,
    data: {
        username: 'TestUser'
    }
});

Mock.mock('/api/signup', 'post', {
    code: 3030,
    data: {
        username: '@name'
    }
});

Mock.mock('/api/logout', 'post', {
    code: 200,
    data: {}
});