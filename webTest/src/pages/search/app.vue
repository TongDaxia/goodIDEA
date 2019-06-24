<template>
    <div id="app">
        <div class="content">
            <no-found v-show="isEmpty" type="0"></no-found>
            <div class="search-box">
                <span class="sel-type" @click="switchType" :class="{ 'menu-active': showDown}">{{typeOpt[type].name}}</span>
                <ul v-show="showDown" class="menu" @touchmove.prevent>
                    <li v-for="(v,i) in typeOpt" @click="selecteType(i)" :class="{'menu-selected':i === type}">{{v.name}}</li>
                </ul>
                <input v-model="keyword" placeholder="请输入公司/产品代码/关键字">
                <span class="btn" @click="search">查询</span>
            </div>
            <ul class="list-box">
                <li v-for="d in list">
                    <a :href="formatLink(d)" target="_blank">{{d.name}} {{d.trcode}}</a>
                </li>
            </ul>
        </div>
    </div>
</template>

<script>
    import common from '@utils/commons';
    import noFound from '../../components/noFound';
    let param = common.getParams();

    export default {
        data () {
            return {
                isEmpty:false,
                keyword:'',
                showDown:false,
                type:1,
                list:[],
                typeOpt:[{
                    type:'trust',
                    name:'信托'
                },{
                    type:'collective',
                    name:'集合'
                }]
            }
        },
        created () {},
        mounted () {
            common.resize();
            common.bindScrollEvt();
        },
        methods: {
            switchType(){
                this.showDown = !this.showDown;
            },
            selecteType(i){
                this.type = i;
                this.showDown = false;
            },
            search(){
                this.$loading.open();
                const req = {...param,text:this.keyword,type:this.typeOpt[this.type].type};
                this.$get('/finchinaAPP/nonStandard/searchNonStandard.action', req).then(res => {
                    this.list = res.data;
                    this.isEmpty = !res.data.length;
                    this.$loading.close();
                }).catch(err=>{
                    this.isEmpty = true;
                    this.$loading.close();
                });
            },
            formatLink(d){
                return common.resolveUrl({
                    path: 'index.html',//location.origin +'/finchinaAPP/hf/privateFund/index.html',
                    queries: {
                        user: param.user,
                        token: param.token,
                        code: d.trcode,
                        from:this.type
                    }
                })
            }
        },
        components: {noFound},
    }
</script>
<style lang="less">
    @import "../../assets/less/base";
    #app {
        min-height: 100%;
        background: #fff;
        .content{
            height: 100%;
            padding: 0.32rem;
            .search-box{
                position: relative;
                .sel-type {
                    color: #FF7500;
                    font-size: 0.26rem;
                    width: 1rem;
                    display: inline-block;
                    line-height: 0.6rem;
                    text-align: center;
                    &:after {
                        content: '';
                        display: inline-block;
                        width: 0.135rem;
                        height: 0.135rem;
                        border: 0.02rem solid;
                        border-color: transparent #FF7500 #FF7500 transparent;
                        transform: rotate(45deg);
                        vertical-align: top;
                        margin: 0.15rem 0 0 0.1rem;
                    }
                    &.menu-active {
                        color: #ff7500;
                        &:after {
                            transform-origin: 0.1rem 0.14rem;
                            transform: rotate(-135deg);
                        }
                    }
                }
                .menu{
                    width: 1.2rem;
                    position: absolute;
                    z-index: 1;
                    top: 0.8rem;
                    border: 0.01rem solid #000;
                    font-size: 0.26rem;
                    border-radius: 0.06rem;
                    padding: 0.1rem 0;
                    background: #fff;
                    &:before{
                        content: '';
                        width: 0.14rem;
                        height: 0.14rem;
                        border: 0.01rem solid;
                        border-color: transparent #000 #000 transparent;
                        transform: rotate(-135deg);
                        position: absolute;
                        top: -0.09rem;
                        right: 0.25rem;
                        background: #fff;
                    }
                    li {
                        font-size: 0.26rem;
                        text-align: center;
                        line-height: 0.6rem;
                        &.menu-selected {
                            color: #ff6b12;
                        }
                    }
                }
                .btn{
                    background: #09b1ce;
                    color: #fff;
                    font-size: 0.26rem;
                    height: 0.6rem;
                    line-height: 0.6rem;
                    display: inline-block;
                    width: 1rem;
                    text-align: center;
                    border: 0.01rem solid #09b1ce;
                    border-radius: 0.1rem;
                    vertical-align: top;
                    margin-left: 0.1rem;
                }
                input{
                    font-size: 0.26rem;
                    height: 0.6rem;
                    width: 4.4rem;
                    outline: none;
                    text-indent: 0.1rem;
                    border: 0.01rem solid #000;
                    padding: 0;
                    border-radius: 0.1rem;
                    /*caret-color: #404040;*/
                    &::-webkit-input-placeholder{
                        line-height:0.4rem;
                    }
                }
            }
            .list-box{
                li{
                    margin-top: 0.32rem;
                }
                a{
                    color: #1482F0;
                    font-size: 0.26rem;
                }
            }
        }
    }
    .indicator {
        .indicator-mask{
            top: 1rem;
            height: calc(100% - 1rem);
        }
    }
</style>
