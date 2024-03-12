import {POST} from '@/utils/request'

export const userList = (params) => {
    return POST(
        {
            url:'/user/list',
            params
        }
    )
}
