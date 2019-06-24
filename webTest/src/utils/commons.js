import {on} from "./dom";
let _ = require('lodash');
export default {
    //动态计算不同屏幕对应的font-size
    resize(designWidth = 750) {
        function changeView() {
            let width = document.body.clientWidth || document.documentElement.clientWidth,
                height = document.body.clientHeight || document.documentElement.clientHeight;
            // if (width > height) width = height;

            // if (width < 320) {
            //     let screen = window.screen;
            //     width = screen[screen.width > screen.height ? 'height' : 'width'];
            //     if (width < 320) width = 320;
            // }

            //if (width > 750) width = 750;

            document.getElementsByTagName('html')[0].style.setProperty('font-size', 100 * width / designWidth + 'px');
            document.getElementsByTagName('body')[0].style.setProperty('visibility', 'visible');
        }

        let ev = 'orientationchange' in window ? 'orientationchange' : 'resize';

        // if (document.addEventListener) {
        //     window.addEventListener(ev, changeView, false);
        // } else if (document.attachEvent) {
        //     window.attachEvent(ev, changeView);
        // } else {
        //     window['on' + ev] = changeView;
        // }
        changeView()
    },
    //body滚动通知app
    bindScrollEvt() {
        let body = document.getElementsByTagName("body")[0];
            // , currentScroll = false
            // , resizing = false, stopScrollHandler = false;
        // 是iOS wkwebview和js交互的方法
        $(window).on('scroll', ()=>{
            try {
                window.jsi.setStick(body.scrollTop > 0);
            } catch (e) {
            }

            try {
                webkit.messageHandlers.setStick.postMessage(body.scrollTop > 0);
            } catch (e) {
            }
        })
    },
    //获取文件类型
    getExtClass(ext) {
        let c = '';
        if (ext == 'jpg' || ext == 'jpeg' || ext == 'tiff' || ext == 'gif' || ext == 'bmp') {
            c = 'pic';
        } else if (ext == 'zip' || ext == 'rar' || ext == '7z') {
            c = 'rar'
        } else if (ext == 'doc' || ext == 'docx') {
            c = 'doc'
        } else if (ext == 'xls' || ext == 'xlsx') {
            c = 'xls'
        } else if (ext == 'txt') {
            c = 'txt'
        } else if (ext == 'pdf') {
            c = 'pdf'
        } else if (ext == 'ppt' || ext == 'pptx') {
            c = 'ppt'
        } else if (ext == 'html' || ext == 'htm') {
            c = 'htm'
        }
        return c;
    },
    //获取页面参数
    getParams(){
        let search = location.search, result = {};
        if(search.length) {
            let arr = search.substr(1).split('&');
            arr.forEach(s=>{
                let t = s.split('=');
                result[t[0]] = decodeURI(t[1])
            })
        }
        return result;
    },
    replaceSepcialTxt(txt){
        let specialArr = '$()*+.[]?\^{}|'.split(''), results = [];
        txt.split('').forEach(s=>{
            results.push(specialArr.indexOf(s) == -1 ? s: '\\' + s);
        });

        return results.join('')
    },
    //获取app类型
    getAppFrom(){
        let appFrom = window.appType;

        try{
            appFrom = window.jsi.getAppType()
        }catch(e){}

        return appFrom;
    },
    //添加水印
    addWaterMark(size = 750){
        let appFrom = this.getAppFrom();
        if(appFrom && appFrom !== 'finchina') return;

        let waterMark = document.createElement('img');

        waterMark.style.cssText = 'position: fixed; z-index: -1; bottom: .36rem; right: .36rem; opacity: 0.0001;';
        waterMark.src = location.origin + '/finchinaAPP/static/waterMark.png';//环境图片

        document.getElementsByTagName('body')[0].appendChild(waterMark);

        let rate = size / 750;
        waterMark.onload = function(){
            waterMark.style.cssText = 'position: fixed; z-index: 9; bottom: '+ (.36 * rate) +'rem; right: '+ (.36 * rate) +'rem; opacity: 0.5;' +
                ' width: '+ (waterMark.naturalWidth / 100 * rate) + 'rem; height: '+ (waterMark.naturalHeight / 100 * rate) + 'rem;';
        }
    },
    /**
     * url查询字符串转换为对象
     * @param {String} urlString url查询字符串
     */
    stringUrlToObject(urlString) {
        const searchObj = {};
        const list = urlString.match(/([^&?]*?=[^&?]*)/g);//转化为键值对的数组
        if (!_.isEmpty(list)) {
            list.forEach(search => {
                const searchSplit = search.split("=");
                searchObj[decodeURIComponent(searchSplit[0])] = decodeURIComponent(searchSplit[1]);//解码
            });
        }
        return searchObj;
    },
    /**
     * 对象转为url查询字符串
     * @param {Object} queryObj 查询对象
     */
    objectUrlToString(queryObj) {
        if (!_.isPlainObject(queryObj)) {//是否是对象
            throw new Error("argument must be an object");
        }
        let query = "";
        Object.keys(queryObj).forEach(key => {
            if (!_.isNil(queryObj[key])) {// isNil:是null或undefined
                query += `&${encodeURIComponent(key)}=${encodeURIComponent(queryObj[key])}`;//编码
            }
        });
        return "?" + query.slice(1);
    },
    /**
     *将逗号拼接的多个名称（code，类型）转换单个object
     */
    showLinks({ name: nameStr = "", code: codeStr = "", type: typeStr = "" }, linkOnly = false) {
        const names = (!_.isEmpty(nameStr) && nameStr.split(",")) || [],
            codes = (!_.isEmpty(codeStr) && codeStr.split(",")) || [],
            types = (!_.isEmpty(typeStr) && typeStr.split(",")) || [];
        let options = codes.map((o,i) => {
            return {
                name: names[i],
                type: types[i] ? types[i] : typeStr,
                code:o
            }
        });
        return this.extractStringToLinks(nameStr, options, linkOnly);
    },
    /**
     * 解析为完整url地址
     */
    resolveUrl({ baseUrl = "", path = "", queries = "", hash }) {
        if (typeof baseUrl !== "string" || typeof path !== "string") {
            throw new Error("baseUrl and path must be a string");
        }
        let handledQueries = "";
        if (typeof queries === "string") {
            handledQueries = this.objectUrlToString(this.stringUrlToObject(queries));
        } else if (_.isPlainObject(queries)) {//是否是对象
            handledQueries = this.objectUrlToString(queries);
        } else {
            throw new Error(
                `${queries} is an error type, string or object is necessary`
            );
        }
        hash = !_.isNil(hash) ? (hash.startsWith("#") ? hash : `#${hash}`) : "";
        return baseUrl + path + handledQueries + hash;
    },
    /**
     * 将链接参数与路径拼接，并渲染为 -- 、a链接 、 链接地址
     * 链接样式：dzh://addBlock?type=testType&name=someName&code=someCode
     * @param namesStr 名称字符串
     * @param options  参数对象数组
     * @param linkOnly   只显示url，不显示a标签
     * @returns {*|string}
     */
    extractStringToLinks(namesStr, options = [], linkOnly, prefix = "dzh://company") {
        if (typeof namesStr === "string") {
            let links = [], urls = [];
            options.forEach((o,i) => {
                urls[i] = _.isEmpty(o.code) ? '' : this.resolveUrl({baseUrl: prefix, queries: o});
            });
            namesStr.split(",").map((n, i) => {
                links[i] = (!n || n ==='--') ? '--' :
                            _.isEmpty(urls[i])
                                ? `<span>${n}</span>`
                                : linkOnly
                                    ? urls[i]
                                    : `<a href="${urls[i]}" style="color:#1482F0" target="_blank">${n}</a>`
            });
            return links.join(",");
        }
        return "";
    },
    /**
     * 日期格式化，如下示例：
     * formatDate('20170101', 'YYYYMMDD', 'YYYY.MM.DD') 输出:2017.01.01
     * formatDate(Date.now(), 'YYYY-MM-DD') 输出：当前时间的格式化
     * @param value
     * @param format 源日期格式
     * @param newFormat 新日期格式
     * @returns {String}
     */
    formatDate (value, format = 'YYYYMMDD', newFormat = 'YYYY-MM-DD') {
        if (!value || value === '--') return '';
        if(value instanceof String && value.includes('19000101')){
            return '';
        }
        const time = new Date(value);
        if (isNaN(+time)) {//把日期从format转为newFormat
            const matches = format.match(/([a-zA-Z])\1{1,}/g);
            let start = 0;
            for (const match of matches) {
                if (newFormat && newFormat.indexOf(match) !== -1) {
                    newFormat = newFormat.replace(new RegExp(match, 'g'), String(value).substr(start, match.length));
                }
                start += match.length;
            }
            return newFormat;
        } else {//把当前日期Date转为format
            const timeMaps = {
                'Y{4}': time.getFullYear(),
                'Y{2}': (time.getFullYear().toString()).substr(2),
                'M{2}': time.getMonth() + 1,
                'D{2}': time.getDate(),
                'H{2}': time.getHours(),
                'm{2}': time.getMinutes(),
                's{2}': time.getSeconds()
            };

            Object.keys(timeMaps).forEach(key => {
                timeMaps[key] = String(timeMaps[key]).padStart(2, 0);
                format = format.replace(new RegExp(key, 'g'), timeMaps[key])
            });
            return format;
        }
    },
    //金额空处理，保留四位小数
    formatMoney(value, fixed = 4) {
        if (!value || isNaN(parseFloat(value))) return '';
        return parseFloat(value).toFixed(fixed);
    },
    //首字母转换大写
    capitalize(value) {
        return (value || value === 0)
            ? value.toString().charAt(0).toUpperCase() + value.toString().slice(1)
            : '';
    },
    //整体转换大写
    upperCase(value) {
        return (value || value === 0)
            ? value.toString().toUpperCase()
            : '';
    },
    //整体转换小写
    lowerCase(value) {
        return (value || value === 0)
            ? value.toString().toLowerCase()
            : '';
    },
    placeholder(input, property) {
        return (input === undefined || input === '' || input === null) ? property : input;
    },
    //字符串截取
    truncate(value, length = 15) {
        if (!value || typeof value !== 'string') return '';
        if (value.length < length) return value;
        return value.substring(0, length) + '...';
    },
    /**
     * 将降幅用红绿色字体显示
     * @param v
     * @returns {{color: string}}
     */
    formatStyle(v,isBold){
        if(v && v !=='--'){
            let style = {color : `${v}`.includes('-') ? '#02A93A' : '#EF3636'};
            if(isBold) style.fontWeight = '600';
            return style;
        }
    },
    /**
     * 数据为空处理
     * @param value 值
     * @param unit 单位
     * @returns {string}
     */
    noEmpty (value,unit='') {
        if (!value || value ==='--') {
            return '--';
        }
        return `${value}${unit}`;
    },
    /**
     * 时间格式处理成
     * 刚刚、5分钟前、10分钟前、半小时前、n小时前、月日、年月日
     * @param value:'2019-04-26 12:12:12'
     * @returns {string}
     */
    dateDesc(value){
        if (!value) return '';
        const now = new Date(),
            time = new Date(value),
            endTime = now.getTime(),
            startTime = time.getTime();
        let str = '' ,second = (endTime - startTime)/1000;//秒
        if (second <= 5*60) {// 5分钟内（含5分钟）
            str ="刚刚";
        }else if (second <= 10*60) {// 5-10分钟内（含10分钟）
            str = '5分钟前';
        }else if (second <= 30*60) {// 10-30分钟内（含30分钟）
            str = '10分钟前';
        }else if (second <= 60*60) {// 30分钟-1小时（含1小时）
            str = '半小时前';
        }else if (second <= 12*60*60){// 在12小时内的每个小时时间段内，以起始时间为标注
            str =`${Math.floor(second/60/60)}小时前`;
        }else if(time.getYear() === now.getYear()){//今年的显示月日
            str = this.formatDate(time,'MM-DD')
        }else if (time.getYear() !== now.getYear()){//非今年显示年月日
            str = this.formatDate(time,'YYYY-MM-DD')
        }
        return str;
    },
    /**整个页面滚动
     * 滚动条距离页面底部的距离
     * @returns {number}
     */
    getScrollBottomHeight (id) {
        const pageHeight = document.documentElement.scrollHeight || document.body.scrollHeight,
            scrollTop = document.documentElement.scrollTop || document.body.scrollTop,//window.pageYOffset
            windowHeight = this.getWindowHeight();
        const conHeight = id ? document.querySelector(id).scrollHeight : pageHeight;
        return conHeight - scrollTop - windowHeight
    },
    /**表体滚动
     * 滚动条距离页面底部的距离
     * @returns {number}
     */
    getTbodyScrollBottom(name){
        if(name){
            let t = document.getElementsByClassName(name)[0];
            return t.scrollHeight - t.scrollTop - t.offsetHeight;
        }else{
            let t = document.getElementsByTagName('tbody')[0];
            return t.scrollHeight - t.scrollTop;
        }
    },
    /**
     * 窗口高度$(window).height()
     * @returns {number}
     */
    getWindowHeight () {
        let windowHeight = 0;
        if (document.compatMode === 'CSS1Compat') {
            windowHeight = document.documentElement.clientHeight
        } else {
            windowHeight = document.body.clientHeight
        }
        return windowHeight
    },
    confOption(){
        const from = this.getParams().from || 0;
        let tab = [{k:'netValue',v:'净值'}, {k:'notice',v:'公告'},
            {k:'product',v:'概况'}, {k:'manager',v:'投资经理'},
            {k:'relatedSecurities',v:'管理人管理的产品'}];
        let time = [{k:'近3个月',v:1},{k:'近6个月',v:2},{k:'近1年',v:3},
            {k:'近3年',v:5},{k:'今年以来',v:6},{k:'成立以来',v:7}];
        if(from == '0'){//私募
            tab.splice(4, 0, {k:'heavyStock',v:'隐形重仓股'});
            time.splice(3,0,{k:'近2年',v:4});
        }
        if(from == '1'){//集合理财
            tab.splice(4, 0, {k:'shareBonus',v:'分红'},
            {k:'investPortfolio',v:'投资组合'},
            {k:'shareChange',v:'份额变动'});
            time.unshift({k:'近1个月',v:0});
        }
        return {tab,time,};
    }
}
