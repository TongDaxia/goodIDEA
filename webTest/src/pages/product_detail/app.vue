<template>
    <div id="app">
        <div class="content" v-if="showInfo">
            <dl v-for="(v,k) in obj.item">
                <dt>{{k}}</dt>
                <dd v-if="k==='投资经理'" style="word-break: keep-all">{{v}}</dd>
                <dd v-else-if="k==='管理人'"
                    v-html="showLinks({name: v,code: obj.code['管理人代码'], type: 'company'})">{{v}}</dd>
                <dd v-else-if="k==='托管人'"
                    v-html="showLinks({name: v,code: obj.code['托管人代码'], type: 'company'})">{{v}}</dd>
                <dd v-else>{{v}}</dd>
            </dl>
        </div>
    </div>
</template>

<script>
    import common from '@utils/commons';
    import noFound from '../../components/noFound'

    let param = common.getParams();

    export default {
        data() {
            return {
                showInfo: false,
                obj:{},
            }
        },
        mounted() {
            common.resize();
            common.bindScrollEvt();

            this.$nextTick(() => {
                common.addWaterMark();
            });
            this.getData();
        },
        methods: {
            showLinks(obj){
                return common.showLinks(obj);
            },
            getData() {
                this.$loading.open();
                const req ={...param, type:'detail'};
                this.$get('/finchinaAPP/nonStandard/getProductOverview.action', req).then(res => {
                    this.$loading.close();
                    this.obj = res.data;
                    this.showInfo = true;
                }).catch(err=>{
                    this.$loading.close();
                    this.showInfo = true;
                });
            }
        },
        components: {noFound},
    }
</script>

<style lang="less">
    @import "../../assets/less/base";

    #app {
        .content{
            padding: 0.32rem 0.32rem 0.48rem 0.32rem;
        }
        dl {
            margin: 0 0 0.3rem;
            line-height: .36rem;
            display: flex;
            justify-content: space-between;
            dt {
                width: 2.5rem;
                font-size: .26rem;
                color: #8C8C8C;
                white-space: nowrap;
                &.three-word {
                    letter-spacing: 0.13rem;
                }
            }

            dd {
                width: calc(100% - 2.5rem);
                margin-left: 0.5rem;
                font-size: .26rem;
                color: #111;
                word-break: break-all;
                text-align: left;
            }
        }

        .flex {
            display: flex;

            > span {
                padding-right: 0.08rem;
            }

            > div:first-of-type {
                padding-right: 0.08rem;
            }

            a {
                color: #111;
                margin-right: 0.08rem;

                &.ext-link {
                    margin-right: 0;
                    font-size: .24rem;
                    color: #1482F0;
                    width: 1.1rem;
                    white-space: nowrap;
                    text-align: right;
                    flex: none;
                    float: right;
                }
            }

            .tags {
                flex: auto;

                &.tag1 {
                    min-width: .55rem;
                }

                &.tag2 {
                    min-width: 1.1rem;
                }

                ul {
                    overflow: hidden;
                    transform: scale(.75);
                    transform-origin: left center;
                    width: 133%;

                    li {
                        font-size: .24rem;
                        color: #78BC6F;
                        background: rgba(133, 192, 94, 0.12);
                        border-radius: 0.04rem;
                        line-height: .34rem;
                        padding: 0 0.04rem;
                        margin: 0 0.08rem 0 0;
                        display: inline-block;

                        &:last-of-type {
                            margin-right: 0;
                        }

                        &.breakFaith {
                            color: #FB7171;
                            background: rgba(253, 183, 183, .36);
                        }
                    }
                }
            }
        }
        .has-icon {
            color: #111;
            margin-right: 0.08rem;
        }
    }
</style>
