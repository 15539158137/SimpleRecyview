# SimpleRecyview
功能慢慢完善中的recycleview
使用方法：
1.关于adapter：
  继承 SimpleRecycleviewAdater
  重写onBindViewHolder：
  if(mList.get(position).getBeanType()==1){
        MyViewHolder myViewHold= (MyViewHolder) holder;
        myViewHold.textView.setText("第"+position+"位置数据");
    }
  重写onCreateViewHolder：
    if(viewType==1){
            View view= LayoutInflater.from(mContext).inflate(R.layout.item,null);
            RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,       ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.height= (int) (MyUtils.getHeight(mContext)*0.1);
            view.setLayoutParams(layoutParams);
            return new MyViewHolder(view);
        }else {
        return super.onCreateViewHolder(parent, viewType);
        }
        
2.关于bean类
  继承SimpleBean
3.关于  Recycleview
  <com.example.shibo.simplerecycleview.recycleview.SimpleRecycleview
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:id="@+id/recycleview"/>
4.关于item间距、刷新布局的修改
  //设置item间距的方法
        recycleview.setItemDe((int) (MyUtils.getHeight(MainActivity.this) * 0.01));
        //自定义头部和底部的layout,里面需要传递一个view过去
        //  mRecycleviewAdapter.setHeadView(LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main, null));
        // mRecycleviewAdapter.setFootView(LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main, null));
5.刷新和加载的回调
   //使用时需注意，传入stopRefreshOrLoad（）的list，不能是adapter中的list，必须是一个其他的list
        recycleview.setOnScrollChanListener(new SimpleRecycleview.OnScrollChanListener() {
            @Override
            public void onRefresh() {
                if(newList.size()>0){
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
     6.注意点
      recycleview的stopRefreshOrLoad方法中传入的list，一定不能是实例化adapter时的list，必须是另外一个list。
