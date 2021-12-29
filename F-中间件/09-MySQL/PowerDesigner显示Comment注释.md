# PowerDesigner显示Comment注释

PowerDesigner默认显示的列是Name及类型，如下图示：

 ![image-20211118144812892](.\images\image-20211118144812892.png)

现在需要显示注释列，以便使得ER图更加清晰。但是PowerDesigner勾选Comment显示没有效果，所以通过以下几步来处理：

双击表，弹出表属性对话框，切到ColumnTab，默认是没显示Comment的，显示Comment列，这么做

 ![image-20211118144859781](.\images\image-20211118144859781.png)

设置显示Comment

 ![image-20211118144945824](.\images\image-20211118144945824.png)

有了Comment列，并补充Comment信息

 ![image-20211118145010330](.\images\image-20211118145010330.png)

确定保存，打开菜单 Tools>Display Perferences..

 ![image-20211118145039780](.\images\image-20211118145039780.png)

调整显示的Attribute，请注意参考下图的选项顺序

 ![image-20211118145100759](.\images\image-20211118145100759.png)

OK，保存，确定，退出设置页，应用到所有标识，可以看到表变化

 ![image-20211118145144806](.\images\image-20211118145144806.png)

接下来需要执行VBS脚本，借鉴网络上的脚本，并且完善了下，处理Comment为空的情形

```js
    Option   Explicit   
    ValidationMode   =   True   
    InteractiveMode   =   im_Batch
    Dim blankStr
    blankStr   =   Space(1)
    Dim   mdl   '   the   current   model  
      
    '   get   the   current   active   model   
    Set   mdl   =   ActiveModel   
    If   (mdl   Is   Nothing)   Then   
          MsgBox   "There   is   no   current   Model "   
    ElseIf   Not   mdl.IsKindOf(PdPDM.cls_Model)   Then   
          MsgBox   "The   current   model   is   not   an   Physical   Data   model. "   
    Else   
          ProcessFolder   mdl   
    End   If  
      
    Private   sub   ProcessFolder(folder)   
    On Error Resume Next  
          Dim   Tab   'running     table   
          for   each   Tab   in   folder.tables   
                if   not   tab.isShortcut   then   
                      tab.name   =   tab.comment  
                      Dim   col   '   running   column   
                      for   each   col   in   tab.columns   
                      if col.comment = "" or replace(col.comment," ", "")="" Then
                            col.name = blankStr
                            blankStr = blankStr & Space(1)
                      else  
                            col.name = col.comment   
                      end if  
                      next   
                end   if   
          next  
      
          Dim   view   'running   view   
          for   each   view   in   folder.Views   
                if   not   view.isShortcut   then   
                      view.name   =   view.comment   
                end   if   
          next  
      
          '   go   into   the   sub-packages   
          Dim   f   '   running   folder   
          For   Each   f   In   folder.Packages   
                if   not   f.IsShortcut   then   
                      ProcessFolder   f   
                end   if   
          Next   
    end   sub  
```

打开菜单Tools>Execute Commands>Edit/Run Script.. 或者用快捷键 Ctrl+Shift+X

 ![image-20211118145238819](.\images\image-20211118145238819.png)

执行完，可以看到第3列显示备注哈哈，效果如下

 ![image-20211118145300168](.\images\image-20211118145300168.png)

原理就是把显示name的列的值，替换成注释的值，所以下次如果调整comment，还有重新执行脚本，所以最好放在最后执行。

