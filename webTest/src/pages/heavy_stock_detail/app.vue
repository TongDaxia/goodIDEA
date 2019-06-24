<template>
    <div id="app">
        <no-found v-if="isEmpty"></no-found>
        <div class="main-box">
            <div class="end-time" v-if="timeOption[0]">截止日期：
                <span class="time-option" @click=" showTimeDown = !showTimeDown;"
                      :class="{ 'menu-active': showTimeDown}">{{selTime}}</span>
            </div>
            <!--时间下拉选项-->
            <div v-show="showTimeDown" class="menu-cover" @touchmove.prevent  @click.self="showTimeDown = false">
                <transition name="slide-down">
                        <ul v-show="showTimeDown" v-scroll-fix>
                            <li v-for="v in timeOption"
                                @click="selecteTime(v)"
                                class="needsclick"
                                :class="{'menu-selected':v === selTime}">{{v}}
                            </li>
                        </ul>
                </transition>
            </div>
            <div class="table-box content" v-if="tableData.length">
                <table class="scroll-tbody">
                    <thead>
                        <tr><th>股票</th><th>市值(万元)</th><th>股数(万股)</th></tr>
                    </thead>
                    <tbody>
                        <tr v-for="d in tableData"><td>{{d.name}}</td><td>{{d.money}}</td><td>{{d.num}}</td></tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</template>

<script>
    import common from '@utils/commons';
    import noFound from '../../components/noFound';
    import scrollFix from '../../directors/scrollFix';
    const params = common.getParams();

    export default {
        data() {
            return {
                timeOption: [params.reportDate],//时间下拉框选项
                showTimeDown: false,//时间下拉框显示隐藏
                selTime: params.reportDate,//选中的时间项
                tableData:[],
                isEmpty:false,
            }
        },
        created(){
            this.getTime();
            this.getList();
        },
        mounted() {
            common.resize();
            common.bindScrollEvt();
            this.$nextTick(() => {
                common.addWaterMark();
            });
        },
        methods: {
            getTime(){
                this.$get('/finchinaAPP/nonStandard/getInvisibleHeavyStockDateList.action', params).then(res => {
                    this.timeOption = res.data.dateList.split(',');
                })
            },
            getList(){
                this.$loading.open();
                this.tableData = [];
                const req ={...params, type:'details',reportDate:this.selTime};
                this.$get('/finchinaAPP/nonStandard/getInvisibleHeavyStockData.action', req).then((res) => {
                    this.$loading.close();
                    this.tableData = res.data.list;
                }).catch(err => {
                    this.$loading.close();
                    this.isEmpty = true;
                });
            },
            selecteTime(v) {
                this.selTime = v;
                this.showTimeDown = false;
                this.getList();
            },
        },
        components: { noFound },
        directives: { scrollFix },
    }
</script>

<style lang="less">
    @import "../../assets/less/base";
    #app {
    	height: 100%;
        .main-box{
            height: 100%;
            .end-time{
                font-size: 0.24rem;
                text-align: right;
                color: #333333;
                line-height: 0.54rem;
                background: #F6F6F6;
                position: fixed;
                top:0;
                z-index: 5;
                width: 100%;
                .time-option {
                    font-size: 0.2rem;
                    margin-right: 0.2rem;
                    position: relative;
                    padding-right: 0.25rem;
                    &:after {
                        content: '';
                        display: block;
                        position: absolute;
                        border: 0.13rem solid transparent;
                        margin-top: -0.05rem;
                        border-top-color: #bbb;
                        right: -0.05rem;
                        top: 50%;
                        transition: all 0.3s;
                        border-radius: 0.07rem;
                    }
                    &.menu-active {
                        color: #ff7500;
                        &:after {
                            margin-top: -0.03rem;
                            transform-origin: center 0.04rem;
                            transform: rotate(180deg);
                            border-top-color: #ff7a15;
                        }
                    }
                }
            }
            /*区间收益下拉框*/
            .slide-down-enter-active {
                animation: slide-down 0.5s;
            }
            .slide-down-leave-active {
                animation: slide-down 0 reverse;
            }
            @keyframes slide-down {
                0% {
                    transform: translate3d(0, -100%, 0);
                }
                100% {
                    transform: translate3d(0);
                }
            }
            .menu-cover {
                position: fixed;
                top: 0;
                margin-top: 0.54rem;
                z-index: 4;
                width: 100%;
                height: 100%;
                background: rgba(17, 17, 17, 0.3);
                ul {
                    background: #fff;
                    padding: 0 0.32rem 0.2rem 0.32rem;
                    width: calc(100% - 0.64rem);
                    max-height: 5.6rem;
                    overflow: auto;
                    -webkit-overflow-scrolling: touch;
                    li {
                        font-size: 0.26rem;
                        text-align: left;
                        border-bottom: 1px solid #eee;
                        line-height: 0.68rem;
                        padding: 0 0.24rem;
                        &.menu-selected {
                            border-color: #ff7a15;
                            color: #ff6b12;
                        }
                    }
                }
            }
            .table-box{
                height: calc(100% - 0.54rem);
                padding-top: 0.54rem;
                table{
                    width: 100%;
                    height: 100%;
                    padding: 0 0 0.06rem;
                    text-align: right;
                    tr{
                        width:calc(100% - 0.64rem);
                        padding: 0 0.32rem;
                    }
                    th,td{
                        &:first-child{
                            text-align: left;
                        }
                    }
                    th{
                        font-weight: 600;
                        font-size: 0.24rem;
                        color: #333;
                        padding: 0.24rem 0 0.3rem 0;
                    }
                    td{
                        font-size: 0.26rem;
                        line-height: 0.26rem;
                        color: #111;
                        padding-bottom: 0.24rem;
                    }
                }
            }

        }
    }
</style>
