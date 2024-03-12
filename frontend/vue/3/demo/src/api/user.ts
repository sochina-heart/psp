import {request} from '@/utils/request'

export const userList = (query:any) => {
    return request({
        url: '/user/list',
        method: 'post',
        params: query
    })
}
