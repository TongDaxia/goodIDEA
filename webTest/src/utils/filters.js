let _ = require('lodash')
import common from '@utils/commons';
export default {
    formatDate:common.formatDate,
    dateDesc(value){
        const formatValue = common.formatDate(value,'YYYYMMDDHHmmss', 'YYYY/MM/DD HH:mm:ss');
        return common.dateDesc(formatValue);
    },
    noEmpty:common.noEmpty,
    formatStyle:common.formatStyle,
    detailLink(path,opt){
        const {user,token,code,from} = common.getParams();
        return common.resolveUrl({
            path: `${path}.html`,
            queries: {...{user,token,code,from},...opt}
        });
    }
}