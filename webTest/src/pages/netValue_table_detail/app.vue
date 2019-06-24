<template>
    <div id="app">
        <no-found v-show="isEmpty" type="0"></no-found>
        <div class="content" v-if="data.length">
            <table class="scroll-tbody">
                <thead><tr><th v-for="t in title">{{t}}</th></tr></thead>
                <tbody id="abc">
                    <tr v-for="row in data">
                        <template  v-for="(d,i) in row">
                            <td v-if="title[i].includes('增长率')" :style="d | formatStyle">{{d}}</td>
                            <td v-else>{{d}}</td>
                        </template>
                    </tr>
                    <!--页面底部status：true:正在加载 false：没有更多数据-->
                    <!--<template v-if="isFullScreen">-->
                        <bottom-status v-show="isLoading && hasMore" :status="true"></bottom-status>
                        <bottom-status v-show="!hasMore && !isEmpty && isFullScreen" :status="false"></bottom-status>
                    <!--</template>-->
                </tbody>
            </table>
        </div>
    </div>
</template>

<script>
    import common from '@utils/commons';
    import noFound from '../../components/noFound';
    import bottomStatus from '../../components/bottomStatus';
    let _ = require('lodash');
    let param = common.getParams();

    export default {
        data () {
            return {
                data: [],
                title:[],
                isLoading: false,
                bottomHeight: 200,
                isEmpty: false,
                hasMore: true,
                isFullScreen:false,
            }
        },
        created () {
            this.loadData()
        },
        mounted () {
            common.resize();
            common.bindScrollEvt();

            this.$nextTick(() => {
                common.addWaterMark();
            });
            addEventListener('touchmove', this.onScroll.bind(this));
        },
        methods: {
            // 页面数据初始化
            loadData () {
                this.$loading.open();
                const req = {...param, pagesize: 15, skip: 0};
                this.$get('/finchinaAPP/nonStandard/getnetValueList.action', req).then((res) => {
                    this.$loading.close();
                    this.title = res.data.name;
                    this.data = res.data.list;
                    this.autoGetNest();//数据未满屏，自动请求下一页
                }).catch(err => {
                    this.$loading.close();
                    this.isEmpty = true;
                    this.hasMore = false;
                });
            },
            onScroll () {
                let t = document.querySelector('tbody');
                if(!this.isFullScreen && (t.scrollHeight - t.clientHeight)){
                    this.isFullScreen = true;
                }
                const isScrollBottom = t.scrollHeight - t.scrollTop - t.clientHeight <= this.bottomHeight;
                if ( isScrollBottom && this.hasMore && !this.isLoading) {
                    this.getNestData();
                }
            },
            //获取下一页数据
            getNestData(){
                this.isLoading = true;
                const req = {...param, pagesize: 15, skip:  this.data.length};
                this.$get('/finchinaAPP/nonStandard/getnetValueList.action', req).then(res => {
                    this.data = this.data.concat(res.data.list);
                    this.isLoading = false;
                }).catch(err=>{
                    this.hasMore = false;
                    this.isLoading = false;
                });
            },
            autoGetNest(){
                this.$nextTick(()=>{
                    let tbody_height = document.querySelector('tbody').clientHeight,
                        thead_height = document.querySelector('thead').clientHeight,
                        window_height = common.getWindowHeight();
                    if(tbody_height < window_height - thead_height){
                        this.getNestData();
                    }
                })
            },
        },
        components: {
            noFound, bottomStatus
        },
    }
</script>
<style lang="less">
    @import "../../assets/less/base";
    #app {
        min-height: 100%;
        background: #fff;
        .content{
            height: 100%;
        }
        table{
            width: 100%;
            height: 100%;
            text-align: right;
            thead{
                font-family: PingFangSC-Medium;
                height: 0.96rem;
            }
            tbody{
                font-family: PingFangSC-Regular;
            }
            tr{
                width: calc(100% - 0.56rem);
                margin: 0 0.28rem;
            }
            th{
                font-size: 0.24rem;
                color: #333333;
                letter-spacing: 0;
                line-height: 0.24rem;

                padding: 0.28rem 0 0.28rem 0;
                &:first-child{
                    text-align: left;
                }
            }
            td{
                font-size: 0.26rem;
                letter-spacing: 0;
                line-height: 0.26rem;
                color:  #111111;
                padding-bottom: 0.30rem;
                &:first-child{
                    text-align: left;
                }
            }
        }
        .bottom-msg {
            padding-top: 0.48rem;
        }
    }
</style>
