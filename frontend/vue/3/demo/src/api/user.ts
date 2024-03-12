import {POST} from '@/utils/request'

// export const userList = (query:any) => {
//     return request({
//         url: '/user/list',
//         method: 'post',
//         params: query
//     })
// }

export const userList = (query:any) => {
    return POST({
        url: '/user/list',
        params: query
    })
}
