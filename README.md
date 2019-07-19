# DynamicLoading
学习一下插件化相关技术......  

[Android动态加载技术 简单易懂的介绍方式](https://segmentfault.com/a/1190000004062866)  

[DL动态加载框架技术文档](https://blog.csdn.net/singwhatiwanna/article/details/40283117)  

[插件化中 Classloader 的加载 dex 分析](http://solart.cc/2016/11/16/plugin_classloader/)

[VirtualApk 资源处理](https://www.notion.so/VirtualAPK-1fce1a910c424937acde9528d2acd537)  

[RePlugin FAQ（这个很有用）](https://github.com/Qihoo360/RePlugin/wiki/FAQ)  


### 公共基础库的坑
- 最容易出现的，就是「插件间版本」导致的问题。「直接调用」一般不会 try-catch，一旦调用的方法「被删改」，则会直接抛出 NoSuchMethodError 或者 ClassNotFoundException 等，对程序稳定性造成极为严重的影响。
- ClassLoader 共享时，需要求各插件不能有「重复的包名和类名」，否则会出现强制类型转换问题
- 多出来 hook 点，因为需要做 `DexPathList` 的反射和修改，涉及到 hook 点了，不排除会有兼容性问题

### 公共基础库推荐做法
- 提供一些基础的插件（如 WebView、分享等），各插件对它是反射调用，接口封装好的前提下，这种做法也是很清晰的
- 如果是公共库的话，每个插件放一份，但混淆时会自动去掉无用类和方法，这样的好处是，公共库的任何版本更新不会影响到所有插件


### 共享资源的坑
- 不推荐「直接使用资源」：插件 A 通过 「R.drawable.common_xxx」来使用插件 B 或主程序的资源
- 和代码一样，资源同样有「插件间版本」导致的问题，而且还更加严重。如果宿主升级那么需要保证原来插件调用的资源 id 不能改变，否则宿主升级后，加载的插件还是拿取的旧版本资源 id，可能会导致资源找不到和错乱情况，所以宿主要从使用插件起保证每个版本被插件所使用的资源不能变化 id
- 机型适配，针对不同机型（如ZTEResources、MiuiResources）等做适配，稳定性上值得考量
- 随着插件的增多，「资源分区ID」会越来越难管控，这对我们来说，同样也是一种挑战

### 共享资源的推荐做法
- 通过反射来引入 View
- 通过使用公共类库（如XML引入的方式）来引入 View
- 获取 Resources 对象，并通过getIdentifier来反射获取资源
