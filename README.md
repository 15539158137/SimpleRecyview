# SimpleRecyview
![](https://ooo.0o0.ooo/2017/06/26/5950add3bcfc5.gif)
功能慢慢完善中的recycleview

**具备基本功能，持续更新**

1.常规使用：xml、间距、刷新加载布局更改等

1.关于adapter;adapter中已经有变量mContext和数据集合mList,

  //设置刷新和加载的监听

        //使用时需注意，传入stopRefreshOrLoad（）的list，不能是adapter中的list，必须是一个其他的list

        recycleview.setOnScrollChanListener(new SimpleRecycleview.OnScrollChanListener() {

            @Override

            public void onRefresh() {

                if(newList.size()&gt;0){

                    newList.remove(newList.size()-1);

                }

                recycleview.stopRefreshOrLoad(newList);

            }

            @Override

            public void loadMore() {

                newList.add(new MyBean());

                recycleview.stopRefreshOrLoad(newList);

            }

            @Override

            public void onTimeOut() {

            }

        });

间距和刷新加载布局的更改

 //设置item间距的方法

        recycleview.setItemDe((int) (MyUtils.getHeight(MainActivity.this) \* 0.01));

        //自定义头部和底部的layout,里面需要传递一个view过去

        //  mRecycleviewAdapter.setHeadView(LayoutInflater.from(MainActivity.this).inflate(R.layout.activity\_main, null));

        // mRecycleviewAdapter.setFootView(LayoutInflater.from(MainActivity.this).inflate(R.layout.activity\_main, null));

2.关于adapter的使用

继承SimpleRecycleviewAdapter，重写onCreateViewHolder和onBindViewHolder方法

  @Override

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        /\*重写该方法，在viewType等于1的时候，写入自己的viewholder\*/

        if (viewType == 1) {

            //这里返回你自己的viewholder

            View view = LayoutInflater.from(mContext).inflate(R.layout.item, null);

            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH\_PARENT, ViewGroup.LayoutParams.MATCH\_PARENT);

            layoutParams.height = (int) (MyUtils.getHeight(mContext) \* 0.1);

            view.setLayoutParams(layoutParams);

            return new MyViewHolder(view);

        } else {

            return super.onCreateViewHolder(parent, viewType);

        }

    }

    @Override

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        super.onBindViewHolder(holder, position);

        if (mList.get(position).getBeanType() == 1) {

            //其他照抄，下面的数据绑定写自己的

            MyViewHolder myViewHold = (MyViewHolder) holder;

            myViewHold.textView.setText(&quot;第&quot; + position + &quot;位置数据&quot;);

        }

    }

3.关于刷新和加载的回调

  //使用时需注意，传入stopRefreshOrLoad（）的list，不能是adapter中的list，必须是一个其他的list

        recycleview.setOnScrollChanListener(new SimpleRecycleview.OnScrollChanListener() {

            @Override

            public void onRefresh() {

                if(newList.size()&gt;0){

                    newList.remove(newList.size()-1);

                }

                recycleview.stopRefreshOrLoad(newList);

            }

            @Override

            public void loadMore() {

                newList.add(new MyBean());

                recycleview.stopRefreshOrLoad(newList);

            }

            @Override

            public void onTimeOut() {

            }

        });

    }

4.关于bean

必须继承SimpleBean

5.注意点

在回调中传入的list不能是adapter的list，必须实例化一个新的list;所有涉及个人的都必须判断beanType==1;
