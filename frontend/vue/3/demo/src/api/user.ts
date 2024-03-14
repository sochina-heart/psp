import {POST} from '@/utils/request'

export const userList = (query:any) => {
    return POST({
        url: '/user/list',
        params: query
    })
}

export const removeUser = (ids: string[]) => {
    return POST({
        url: '/user/remove',
        params: {ids}
    })
}
