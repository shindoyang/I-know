# 实验环境

win10系统

安装：git 

安装：VirtualBox-5.2.22-126460-Win

安装：vagrant2.2.3

安装：xshell



centos7.4.box

我的用户主目录：C:\Users\cpf\.vagrant.d



# 操作vagrant box

1. 查看已有box

   ```sh
   #查看已有的box。
   cpf@LAPTOP-BH5NFMO1 MINGW64 /c/vagrant
   $ vagrant.exe box list
   There are no installed boxes! Use `vagrant box add` to add some.
   
   cpf@LAPTOP-BH5NFMO1 MINGW64 /c/vagrant
   #发现没有box
   ```

2. 导入box

   ```sh
   cpf@LAPTOP-BH5NFMO1 MINGW64 /c/vagrant
   $ vagrant.exe box add --name centos-7.4-base /c/vagrant/centos-7.4-base.box
   ==> box: Box file was not detected as metadata. Adding it directly...
   ==> box: Adding box 'centos-7.4-base' (v0) for provider:
       box: Unpacking necessary files from: file:///C:/vagrant/centos-7.4-base.box
       box:
   ==> box: Successfully added box 'centos-7.4-base' (v0) for 'virtualbox'!
   
   cpf@LAPTOP-BH5NFMO1 MINGW64 /c/vagrant
   
   $ vagrant.exe box list
   centos-7.4-base (virtualbox, 0)
   
   cpf@LAPTOP-BH5NFMO1 MINGW64 /c/vagrant
   $
   
   
   ```



3. 实操命令：

![1586841979020](C:/Users/yanggeng/Desktop/assets/1586841979020.png)



# 创建虚拟机

准备3台虚拟机

1. 创建istio目录

   ```sh
   cpf@LAPTOP-BH5NFMO1 MINGW64 /c/vagrant
   $ mkdir istio/
   
   cpf@LAPTOP-BH5NFMO1 MINGW64 /c/vagrant
   $ cd istio/
   ```

2. 编辑Vagrantfile

   ```sh
   $ vi Vagrantfile
   ```

   文件内容：

   ```sh
   Vagrant.configure("2") do |config|
           # 创建3台虚拟机
           (1..3).each do |i|
                   config.vm.define "lab#{i}" do |node|
                           # 表示使用名为：centos-7.4-base的  box
                           node.vm.box = "centos-7.4-base"
                           # 表示不自动生成新的ssh key，使用Vagrant默认的ssh key注入到虚拟机中，这么做主要是为了方便登录。不用为每台虚拟机设置ssh key登录。
                           node.ssh.insert_key=false
                           # 设置虚拟机的主机名，设置3台虚拟机主机名分别为lab1、lab2、lab3
                           node.vm.hostname="lab#{i}"
                           # 设置虚拟机的IP，设置3台虚拟机的私有网络为11.11.11.111、11.11.11.112、11.11.11.113。
                           node.vm.network "private_network", ip: "11.11.11.11#{i}"
                           # 表示当虚拟机启动完成之后，执行inline中配置的shell命令，此命令用于开机之后的时间同步。
                           node.vm.provision "shell", run:"always",
                                   inline:"ntpdate ntp.api.bz"
                           # 表示当虚拟机启动完成之后，执行inline中配置的shell命令，此命令用于输出测试字符串。
                           node.vm.provision "shell", run:"always",
                                   inline:"echo hello from lab#{i}"
                           # VirtaulBox相关配置，设置虚拟机的CPU核心数和内存大小，本次实验设置为每台虚拟机2核2G内存，可以根据自己电脑的实际情况适当加大。
                           node.vm.provider "virtualbox" do |v|
                                   # 设置虚拟机的CPU个数
                                   v.cpus = 2
                                   v.customize ["modifyvm", :id, "--name", "lab#{i}", "--memory", "2048"]
                           end
                   end
           end
   end
   ```

3. 启动虚拟机集群

   ```sh
   $ vagrant.exe status
   Current machine states:
   
   lab1                      not created (virtualbox)
   lab2                      not created (virtualbox)
   lab3                      not created (virtualbox)
   
   This environment represents multiple VMs. The VMs are all listed
   above with their current state. For more information about a specific
   VM, run `vagrant status NAME`.
   
   cpf@LAPTOP-BH5NFMO1 MINGW64 /c/vagrant/istio
   $ vagrant.exe up
   Bringing machine 'lab1' up with 'virtualbox' provider...
   Bringing machine 'lab2' up with 'virtualbox' provider...
   Bringing machine 'lab3' up with 'virtualbox' provider...
   
   ```

   启动完成后：我们看到：

   ```sh
   hello from lab1
   hello from lab2
   hello from lab3
   ```

   查看状态

   ```sh
   $ vagrant.exe status
   Current machine states:
   
   lab1                      running (virtualbox)
   lab2                      running (virtualbox)
   lab3                      running (virtualbox)
   
   This environment represents multiple VMs. The VMs are all listed
   above with their current state. For more information about a specific
   VM, run `vagrant status NAME`.
   
   ```

4. 导入密钥

   使用Xshell导入vagrant的密钥。密钥存储在VAGRANT_HOME环境变量里指定目录的insecure_private_key文件中，添加该密钥到Xshell中。选择顶部菜单中“工具”菜单的“用户密钥管理者”，在弹出的对话框中点击“导入”按钮，选择insecure_private_key文件即可完成密钥的导入

   ```sh
   找到目录：C:\Users\cpf\.vagrant.d，下的文件：insecure_private_key，即是密钥文件。
   
   打开xshell，
   工具-》用户密钥管理者-》导入，选择上面文件。
   
   新建连接：
   ssh；ip：11.11.11.111,端口22.
   用户身份验证：方法public_key,用户名：vagrant，用户密钥：insecure_private_key。
   ```



![1586843948085](C:/Users/yanggeng/Desktop/assets/1586843948085.png)



![1586843957595](C:/Users/yanggeng/Desktop/assets/1586843957595.png)



5. 测试

   ![1586844269655](C:/Users/yanggeng/Desktop/assets/1586844269655.png)



![1586844281536](C:/Users/yanggeng/Desktop/assets/1586844281536.png)



![1586844290558](C:/Users/yanggeng/Desktop/assets/1586844290558.png)



# 保存环境快照

```sh
cpf@LAPTOP-BH5NFMO1 MINGW64 /c/vagrant/istio
$ vagrant.exe snapshot list
==> lab1: No snapshots have been taken yet!
    lab1: You can take a snapshot using `vagrant snapshot save`. Note that
    lab1: not all providers support this yet. Once a snapshot is taken, you
    lab1: can list them using this command, and use commands such as
    lab1: `vagrant snapshot restore` to go back to a certain snapshot.
==> lab2: No snapshots have been taken yet!
    lab2: You can take a snapshot using `vagrant snapshot save`. Note that
    lab2: not all providers support this yet. Once a snapshot is taken, you
    lab2: can list them using this command, and use commands such as
    lab2: `vagrant snapshot restore` to go back to a certain snapshot.
==> lab3: No snapshots have been taken yet!
    lab3: You can take a snapshot using `vagrant snapshot save`. Note that
    lab3: not all providers support this yet. Once a snapshot is taken, you
    lab3: can list them using this command, and use commands such as
    lab3: `vagrant snapshot restore` to go back to a certain snapshot.

cpf@LAPTOP-BH5NFMO1 MINGW64 /c/vagrant/istio
$ vagrant.exe snapshot save base
==> lab1: Snapshotting the machine as 'base'...
==> lab1: Snapshot saved! You can restore the snapshot at any time by
==> lab1: using `vagrant snapshot restore`. You can delete it using
==> lab1: `vagrant snapshot delete`.
==> lab2: Snapshotting the machine as 'base'...
==> lab2: Snapshot saved! You can restore the snapshot at any time by
==> lab2: using `vagrant snapshot restore`. You can delete it using
==> lab2: `vagrant snapshot delete`.
==> lab3: Snapshotting the machine as 'base'...
==> lab3: Snapshot saved! You can restore the snapshot at any time by
==> lab3: using `vagrant snapshot restore`. You can delete it using
==> lab3: `vagrant snapshot delete`.

cpf@LAPTOP-BH5NFMO1 MINGW64 /c/vagrant/istio
$ vagrant.exe snapshot list
base
base
base

cpf@LAPTOP-BH5NFMO1 MINGW64 /c/vagrant/istio
$

```



如果遇到vagrant 问题，基本上 重启就能解决。

# 修改密码登录

```tex
修改root，root登陆。
1.查看服务器配置信息。
vagrant 第一次登陆：
$ vagrant.exe ssh-config
Host default
  HostName 127.0.0.1
  User vagrant
  Port 2222
  UserKnownHostsFile /dev/null
  StrictHostKeyChecking no
  PasswordAuthentication no
  IdentityFile C:/vagrant/tools/.vagrant/machines/default/virtualbox/private_key
  IdentitiesOnly yes
  LogLevel FATAL

2.用密钥登陆，找到上面IdentityFile C:/vagrant/tools/.vagrant/machines/default/virtualbox/private_key。登陆。
3.修改root密码。

[vagrant@localhost ~]$ sudo -i
[root@localhost ~]# passwd
Changing password for user root.
New password: 
BAD PASSWORD: The password is shorter than 8 characters
Retype new password: 
passwd: all authentication tokens updated successfully.
[root@localhost ~]# 

4.更改sshd_config配置信息。
[root@localhost ~]# vi /etc/ssh/sshd_config

# Authentication:

#LoginGraceTime 2m
PermitRootLogin yes（主要是这句）
PasswordAuthentication yes（主要是这句）
#StrictModes yes
#MaxAuthTries 6
#MaxSessions 10

#PubkeyAuthentication yes

5.重启：systemctl restart sshd
```





# 安装docker

1. 在3台机器上操作。可以和k8s一起安装。

2. 移除原来docker

   ```sh
   sudo yum remove docker docker-client docker-client-latest docker-common docker-latest docker-latest-logrotate docker-logrotate docker-selinux docker-engine-selinux docker-engine
   ```



​	![1586845411036](C:/Users/yanggeng/Desktop/assets/1586845411036.png)

2. 安装依赖

   ```sh
   sudo yum install -y yum-utils device-mapper-persistent-data lvm2 wget
   ```



![1586845948147](C:/Users/yanggeng/Desktop/assets/1586845948147.png)



3. 下载发行版repo文件

   ```sh
   sudo wget -O /etc/yum.repos.d/docker-ce.repo https://download.docker.com/linux/centos/docker-ce.repo
   ```

![1586846829857](C:/Users/yanggeng/Desktop/assets/1586846829857.png)



4. 替换源

   ```sh
   # 官方的源速度太慢 把软件仓库地址替换为 TUNA
   sudo sed -i 's+download.docker.com+mirrors.tuna.tsinghua.edu.cn/docker-ce+' /etc/yum.repos.d/docker-ce.repo
   ```

5. 更新源信息

   ```sh
   sudo yum -y makecache fast
   ```

   

6. 安装docker

   ```sh
    sudo yum -y install docker-ce
   ```

   

   ![1586847686890](C:/Users/yanggeng/Desktop/assets/1586847686890.png)

7. 测试docker

   启动docker，并测试docker命令

   ```sh
   [vagrant@lab1 ~]$ sudo systemctl start docker
   [vagrant@lab1 ~]$ sudo docker ps
   CONTAINER ID        IMAGE               COMMAND             CREATED             STATUS              PORTS               NAMES
   [vagrant@lab1 ~]$ 
   ```

8. 配置国内docker官方镜像加速

   ```sh
   sudo tee /etc/docker/daemon.json <<-'EOF'
   {
     "registry-mirrors": ["https://registry.docker-cn.com"]
   }
   EOF
   ```

9. 重载配置

   ```sh
   sudo systemctl daemon-reload
   ```

10. 重启docker

    ```sh
    sudo systemctl restart docker
    ```

    

11. 运行一个docker镜像，输出hello world

    ```sh
    sudo docker run --rm alpine echo 'hello world !'
    ```

12. 查看已经有的镜像

    ```sh
    [vagrant@lab3 ~]$ sudo docker images
    REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
    alpine              latest              a187dde48cd2        3 weeks ago         5.6MB
    [vagrant@lab3 ~]$ 
    ```



# 导出导入镜像

导出：

```sh
docker save -o my_ubuntu_v3.tar runoob/ubuntu:v3
```



导入：

```sh
docker load -i my_ubuntu_v3.tar
```





# 安装k8s

1. 添加阿里云k8s源

   ```sh
   sudo tee /etc/yum.repos.d/kubernetes.repo <<-EOF
   [kubernetes]
   name=Kubernetes
   baseurl=https://mirrors.aliyun.com/kubernetes/yum/repos/kubernetes-el7-x86_64/
   enabled=1
   gpgcheck=1
   repo_gpgcheck=1
   gpgkey=https://mirrors.aliyun.com/kubernetes/yum/doc/yum-key.gpg https://mirrors.aliyun.com/kubernetes/yum/doc/rpm-package-key.gpg
   EOF
   ```

2. 查看k8s版本

   ```sh
   yum list kubelet --showduplicates | sort -r
   ```

3. 安装

   ```sh
   sudo yum install -y docker-ce-18.09.9 kubelet kubeadm kubectl
   
   ```

此时的版本是1.18.1

   ```
   
   ![1586850689671](.\assets\1586850689671.png)
   
4. 查看安装的k8s

​```sh
yum list installed

[vagrant@lab1 ~]$ yum list installed | grep kube
cri-tools.x86_64                1.13.0-0                       @kubernetes      
kubeadm.x86_64                  1.18.1-0                       @kubernetes      
kubectl.x86_64                  1.18.1-0                       @kubernetes      
kubelet.x86_64                  1.18.1-0                       @kubernetes      
kubernetes-cni.x86_64           0.7.5-0                        @kubernetes      
[vagrant@lab1 ~]$ 
   ```





![1586850781972](C:/Users/yanggeng/Desktop/assets/1586850781972.png)



# k8s集群之前准备

1. 设置开机启动

```sh
sudo systemctl start docker
sudo systemctl enable docker

sudo systemctl start kubelet
sudo systemctl enable kubelet

查看开机启动项：
systemctl list-unit-files | grep enabled
```

2. 关闭防火墙

   ```sh
   sudo systemctl stop firewalld
   sudo systemctl disable firewalld
   
   #临时关闭SELinux,杀毒软件
   $ sudo setenforce 0
   #关闭开机启用SELinux
   $ sudo sed -i 's/SELINUX=permissive/SELINUX=disabled/' /etc/sysconfig/selinux
   
   
   ```

3. 关闭swap

   ```sh
   #临时关闭Swap
   $ sudo swapoff -a
   #关闭开机自动挂载Swap分区
   $ sudo sed -ri 's@(^/.*swap.*)@#\1@g' /etc/fstab
   #查看内存情况，Swap为0表示已经关闭成功
   free -m
   ```

   样例：

   ![1586851814192](C:/Users/yanggeng/Desktop/assets/1586851814192.png)

4. 加载IPVS相关内核模块

   在xshell中，输入一次命令，多个会话执行。

   打开xshell，打开查看，显示：撰写栏。

   在撰写栏底部，输入命令，选 当前会话，还是全部会话。

   ```sh
   $ sudo modprobe ip_vs
   $ sudo modprobe ip_vs_rr
   $ sudo modprobe ip_vs_wrr
   $ sudo modprobe ip_vs_sh
   $ sudo modprobe nf_conntrack_ipv4
   #查看IPVS相关内核模块是否导入成功
   $ sudo lsmod | grep ip_vs
   ip_vs_sh       12688   0 
   ip_vs_wrr      12697   0 
   ip_vs_rr       12600   0 
   ip_vs          141092  6 ip_vs_rr,ip_vs_sh,ip_vs_wrr
   nf_conntrack   133387  7 ip_vs,nf_nat,nf_nat_ipv4,xt_conntrack,nf_nat_
       masquerade_ipv4,nf_conntrack_netlink,nf_conntrack_ipv4
   libcrc32c      12644  4 xfs,ip_vs,nf_nat,nf_conntrack
   #配置开机自动导入IPVS相关内核模块
   $ sudo tee /etc/modules-load.d/k8s-ipvs.conf <<-'EOF'
   ip_vs
   ip_vs_rr
   ip_vs_wrr
   ip_vs_sh
   nf_conntrack_ipv4
   EOF
   #查看IPVS相关内核模块配置
   $ sudo cat /etc/modules-load.d/k8s-ipvs.conf
   ```

5. RHEL/CentOS 7需要的特殊配置

   ```sh
   $ sudo tee /etc/sysctl.d/k8s.conf <<-'EOF'
   net.bridge.bridge-nf-call-ip6tables = 1
   net.bridge.bridge-nf-call-iptables = 1
   vm.swappiness=0
   EOF
   #使配置生效
   $ sudo sysctl --system
   ...
   * Applying /etc/sysctl.d/k8s.conf ...
   net.bridge.bridge-nf-call-ip6tables = 1
   net.bridge.bridge-nf-call-iptables = 1
   vm.swappiness = 0
   * Applying /etc/sysctl.conf ...
   ```

6. 开启Forward。Docker从1.13版本开始调整了默认的防火墙规则，禁用了iptables filter表中FOWARD链，这可能会引起Kubernetes集群中跨Node的Pod无法正常通信。如果碰到上述问题，可以使用如下方式开启：

   ```sh
   $ sudo iptables -P FORWARD ACCEPT
   $ sudo sed -i '/ExecStart/a ExecStartPost=/sbin/iptables -P FORWARD ACCEPT' /usr/lib/systemd/system/docker.service
   $ sudo systemctl daemon-reload
   ```

7. 配置Hosts解析

   ```sh
   sudo tee /etc/hosts <<-'EOF'
   127.0.0.1   localhost localhost.localdomain localhost4 localhost4.localdomain4
   ::1         localhost localhost.localdomain localhost6 localhost6.localdomain6
   11.11.11.111 lab1
   11.11.11.112 lab2
   11.11.11.113 lab3
   EOF
   ```

   测试

   ```sh
   ping -c2 lab1
   ping -c2 lab2
   ping -c2 lab3
   
   ```

# 配置Kubelet：

```sh
$ DOCKER_CGROUPS=$(sudo docker info | grep 'Cgroup' | cut -d' ' -f3)
$ echo $DOCKER_CGROUPS
$ sudo tee /etc/sysconfig/kubelet <<-EOF
KUBELET_EXTRA_ARGS="--cgroup-driver=$DOCKER_CGROUPS --pod-infra-container-
    image=registry.cn-hangzhou.aliyuncs.com/google_containers/pause-amd64:3.1"
EOF
#查看配置
$ sudo cat /etc/sysconfig/kubelet
KUBELET_EXTRA_ARGS="--cgroup-driver=cgroupfs --pod-infra-container-image=registry.
    cn-hangzhou.aliyuncs.com/google_containers/pause-amd64:3.1"
#重新加载配置
$ sudo systemctl daemon-reload
```

# 集群规划

1. 规划

   ![1586853146289](C:/Users/yanggeng/Desktop/assets/1586853146289.png)

2. 网卡介绍

   每台虚拟机有两张网卡，分别为eth0、eth1。eth0用于连接外网，安装下载软件，eth1用来集群内部通信。

3. 查看开机启动项

   ```sh
   systemctl list-unit-files | grep enabled
   ```



![1586853371336](C:/Users/yanggeng/Desktop/assets/1586853371336.png)

# 创建master节点（lab1上操作）

1. 初始化主节点

   ```sh
   sudo kubeadm init --apiserver-advertise-address=11.11.11.111 --image-repository registry.aliyuncs.com/google_containers --kubernetes-version v1.18.1 --pod-network-cidr=10.244.0.0/16
   ```

   漫长等待后

   ```sh
   Your Kubernetes control-plane has initialized successfully!
   
   To start using your cluster, you need to run the following as a regular user:
   
     mkdir -p $HOME/.kube
     sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
     sudo chown $(id -u):$(id -g) $HOME/.kube/config
   
   You should now deploy a pod network to the cluster.
   Run "kubectl apply -f [podnetwork].yaml" with one of the options listed at:
     https://kubernetes.io/docs/concepts/cluster-administration/addons/
   
   Then you can join any number of worker nodes by running the following on each as root:
   
   kubeadm join 11.11.11.111:6443 --token nxkd10.l811dvb8bkjn0ehr \
       --discovery-token-ca-cert-hash sha256:b8aff2691ed748a30330c5608149dd63eeaa5b5422f03f4d424a389cbbbce0b0 
   [vagrant@lab1 ~]$ 
   ```

2. 通过xshell打开另一个lab1

   应该先执行：rm -rf $HOME/.kube，但我没执行。哈哈。

   ```sh
   [vagrant@lab1 ~]$ kubectl get nodes
   error: no configuration has been provided, try setting KUBERNETES_MASTER environment variable
   执行：上面提示的语句：
   [vagrant@lab1 ~]$ mkdir -p $HOME/.kube
   [vagrant@lab1 ~]$ sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
   [vagrant@lab1 ~]$ sudo chown $(id -u):$(id -g) $HOME/.kube/config
   [vagrant@lab1 ~]$ 
   [vagrant@lab1 ~]$ kubectl get node
   NAME   STATUS     ROLES    AGE   VERSION
   lab1   NotReady   master   28m   v1.18.1
   [vagrant@lab1 ~]$ 
   ```

   

   样例：

   ![1586855944527](C:/Users/yanggeng/Desktop/assets/1586855944527.png)

   

   回到第一个lab1的tab。

   ```sh
   [vagrant@lab1 ~]$ kubectl get node
   NAME   STATUS     ROLES    AGE   VERSION
   lab1   NotReady   master   28m   v1.18.1
   [vagrant@lab1 ~]$ 
   ```

   

   PS:下面先不用执行。

   ```sh
   由于实验环境虚拟机内存可能不足，导致后面的Istio实验无法成功，可以让master节点和其他node节点一样接收负载，分担其他node节点的压力，但也可能使master节点压力过大，导致master上kubernetes组件出现异常，实验无法成功。只有当不配置此步骤实验就不能继续进行时，才推荐执行此步骤，本书每台虚拟机2G内存的实验环境中并不需要此步骤。使用如下命令设置：
   $ kubectl taint nodes lab1 node-role.kubernetes.io/master-node/lab1 untainted
   ```

   

   # lab2 和 lab3 加入

   在lab2和lab3上执行，master 初始化之后，最后的打印语句：join。

   ```sh
   sudo kubeadm join 11.11.11.111:6443 --token nxkd10.l811dvb8bkjn0ehr --discovery-token-ca-cert-hash sha256:b8aff2691ed748a30330c5608149dd63eeaa5b5422f03f4d424a389cbbbce0b0
   ```

   

   样例：

   ```sh
   [vagrant@lab2 ~]$ sudo kubeadm join 11.11.11.111:6443 --token nxkd10.l811dvb8bkjn0ehr --discovery-token-ca-cert-hash sha256:b8aff2691ed748a30330c5608149dd63eeaa5b5422f03f4d424a389cbbbce0b0
   W0414 17:29:13.299222    9729 join.go:346] [preflight] WARNING: JoinControlPane.controlPlane settings will be ignored when control-plane flag is not set.
   [preflight] Running pre-flight checks
   	[WARNING IsDockerSystemdCheck]: detected "cgroupfs" as the Docker cgroup driver. The recommended driver is "systemd". Please follow the guide at https://kubernetes.io/docs/setup/cri/
   [preflight] Reading configuration from the cluster...
   [preflight] FYI: You can look at this config file with 'kubectl -n kube-system get cm kubeadm-config -oyaml'
   [kubelet-start] Downloading configuration for the kubelet from the "kubelet-config-1.18" ConfigMap in the kube-system namespace
   [kubelet-start] Writing kubelet configuration to file "/var/lib/kubelet/config.yaml"
   [kubelet-start] Writing kubelet environment file with flags to file "/var/lib/kubelet/kubeadm-flags.env"
   [kubelet-start] Starting the kubelet
   [kubelet-start] Waiting for the kubelet to perform the TLS Bootstrap...
   
   This node has joined the cluster:
   * Certificate signing request was sent to apiserver and a response was received.
   * The Kubelet was informed of the new secure connection details.
   
   Run 'kubectl get nodes' on the control-plane to see this node join the cluster.
   ```

   

   ![1586856627765](C:/Users/yanggeng/Desktop/assets/1586856627765.png)

   

   ![1586856650825](C:/Users/yanggeng/Desktop/assets/1586856650825.png)

   

   lab3执行：上面同样join。

   ![1586856705689](C:/Users/yanggeng/Desktop/assets/1586856705689.png)

   

   最后 集群中节点

   ```sh
   [vagrant@lab1 ~]$ kubectl get node
   NAME   STATUS     ROLES    AGE     VERSION
   lab1   NotReady   master   40m     v1.18.1
   lab2   NotReady   <none>   2m18s   v1.18.1
   lab3   NotReady   <none>   5s      v1.18.1
   [vagrant@lab1 ~]$ 
   ```

   # 部署Kubernetes网络插件

   使用Kubeadm创建Kubernetes集群时，如果没有部署网络插件，所有的节点状态都会是NotReady。当网络插件部署完成后，状态会更新为Ready，并会启动CoreDNS，用于集群中的服务发现。

   在lab1上：

   1. 创建kube-flannel.yml文件。

   打开：https://github.com/coreos/flannel/blob/master/Documentation/kube-flannel.yml，复制文件。

   并修改：

   ```sh
   记住改网卡：- --iface=eth1
         containers:
         - name: kube-flannel
           image: quay.io/coreos/flannel:v0.12.0-amd64
           command:
           - /opt/bin/flanneld
           args:
           - --ip-masq
           - --kube-subnet-mgr
           - --iface=eth1
   ```

   

   vi kube-flannel.yml

   正确内容如下：

   ```sh
   ---
   apiVersion: policy/v1beta1
   kind: PodSecurityPolicy
   metadata:
     name: psp.flannel.unprivileged
     annotations:
       seccomp.security.alpha.kubernetes.io/allowedProfileNames: docker/default
       seccomp.security.alpha.kubernetes.io/defaultProfileName: docker/default
       apparmor.security.beta.kubernetes.io/allowedProfileNames: runtime/default
       apparmor.security.beta.kubernetes.io/defaultProfileName: runtime/default
   spec:
     privileged: false
     volumes:
       - configMap
       - secret
       - emptyDir
       - hostPath
     allowedHostPaths:
       - pathPrefix: "/etc/cni/net.d"
       - pathPrefix: "/etc/kube-flannel"
       - pathPrefix: "/run/flannel"
     readOnlyRootFilesystem: false
     # Users and groups
     runAsUser:
       rule: RunAsAny
     supplementalGroups:
       rule: RunAsAny
     fsGroup:
       rule: RunAsAny
     # Privilege Escalation
     allowPrivilegeEscalation: false
     defaultAllowPrivilegeEscalation: false
     # Capabilities
     allowedCapabilities: ['NET_ADMIN']
     defaultAddCapabilities: []
     requiredDropCapabilities: []
     # Host namespaces
     hostPID: false
     hostIPC: false
     hostNetwork: true
     hostPorts:
     - min: 0
       max: 65535
     # SELinux
     seLinux:
       # SELinux is unused in CaaSP
       rule: 'RunAsAny'
   ---
   kind: ClusterRole
   apiVersion: rbac.authorization.k8s.io/v1beta1
   metadata:
     name: flannel
   rules:
     - apiGroups: ['extensions']
       resources: ['podsecuritypolicies']
       verbs: ['use']
       resourceNames: ['psp.flannel.unprivileged']
     - apiGroups:
         - ""
       resources:
         - pods
       verbs:
         - get
     - apiGroups:
         - ""
       resources:
         - nodes
       verbs:
         - list
         - watch
     - apiGroups:
         - ""
       resources:
         - nodes/status
       verbs:
         - patch
   ---
   kind: ClusterRoleBinding
   apiVersion: rbac.authorization.k8s.io/v1beta1
   metadata:
     name: flannel
   roleRef:
     apiGroup: rbac.authorization.k8s.io
     kind: ClusterRole
     name: flannel
   subjects:
   - kind: ServiceAccount
     name: flannel
     namespace: kube-system
   ---
   apiVersion: v1
   kind: ServiceAccount
   metadata:
     name: flannel
     namespace: kube-system
   ---
   kind: ConfigMap
   apiVersion: v1
   metadata:
     name: kube-flannel-cfg
     namespace: kube-system
     labels:
       tier: node
       app: flannel
   data:
     cni-conf.json: |
       {
         "name": "cbr0",
         "cniVersion": "0.3.1",
         "plugins": [
           {
             "type": "flannel",
             "delegate": {
               "hairpinMode": true,
               "isDefaultGateway": true
             }
           },
           {
             "type": "portmap",
             "capabilities": {
               "portMappings": true
             }
           }
         ]
       }
     net-conf.json: |
       {
         "Network": "10.244.0.0/16",
         "Backend": {
           "Type": "vxlan"
         }
       }
   ---
   apiVersion: apps/v1
   kind: DaemonSet
   metadata:
     name: kube-flannel-ds-amd64
     namespace: kube-system
     labels:
       tier: node
       app: flannel
   spec:
     selector:
       matchLabels:
         app: flannel
     template:
       metadata:
         labels:
           tier: node
           app: flannel
       spec:
         affinity:
           nodeAffinity:
             requiredDuringSchedulingIgnoredDuringExecution:
               nodeSelectorTerms:
                 - matchExpressions:
                     - key: beta.kubernetes.io/os
                       operator: In
                       values:
                         - linux
                     - key: beta.kubernetes.io/arch
                       operator: In
                       values:
                         - amd64
         hostNetwork: true
         tolerations:
         - operator: Exists
           effect: NoSchedule
         serviceAccountName: flannel
         initContainers:
         - name: install-cni
           image: quay.io/coreos/flannel:v0.12.0-amd64
           command:
           - cp
           args:
           - -f
           - /etc/kube-flannel/cni-conf.json
           - /etc/cni/net.d/10-flannel.conflist
           volumeMounts:
           - name: cni
             mountPath: /etc/cni/net.d
           - name: flannel-cfg
             mountPath: /etc/kube-flannel/
         containers:
         - name: kube-flannel
           image: quay.io/coreos/flannel:v0.12.0-amd64
           command:
           - /opt/bin/flanneld
           args:
           - --ip-masq
           - --kube-subnet-mgr
           - --iface=eth1
           resources:
             requests:
               cpu: "100m"
               memory: "50Mi"
             limits:
               cpu: "100m"
               memory: "50Mi"
           securityContext:
             privileged: false
             capabilities:
               add: ["NET_ADMIN"]
           env:
           - name: POD_NAME
             valueFrom:
               fieldRef:
                 fieldPath: metadata.name
           - name: POD_NAMESPACE
             valueFrom:
               fieldRef:
                 fieldPath: metadata.namespace
           volumeMounts:
           - name: run
             mountPath: /run/flannel
           - name: flannel-cfg
             mountPath: /etc/kube-flannel/
         volumes:
           - name: run
             hostPath:
               path: /run/flannel
           - name: cni
             hostPath:
               path: /etc/cni/net.d
           - name: flannel-cfg
             configMap:
               name: kube-flannel-cfg
   ---
   apiVersion: apps/v1
   kind: DaemonSet
   metadata:
     name: kube-flannel-ds-arm64
     namespace: kube-system
     labels:
       tier: node
       app: flannel
   spec:
     selector:
       matchLabels:
         app: flannel
     template:
       metadata:
         labels:
           tier: node
           app: flannel
       spec:
         affinity:
           nodeAffinity:
             requiredDuringSchedulingIgnoredDuringExecution:
               nodeSelectorTerms:
                 - matchExpressions:
                     - key: beta.kubernetes.io/os
                       operator: In
                       values:
                         - linux
                     - key: beta.kubernetes.io/arch
                       operator: In
                       values:
                         - arm64
         hostNetwork: true
         tolerations:
         - operator: Exists
           effect: NoSchedule
         serviceAccountName: flannel
         initContainers:
         - name: install-cni
           image: quay.io/coreos/flannel:v0.12.0-arm64
           command:
           - cp
           args:
           - -f
           - /etc/kube-flannel/cni-conf.json
           - /etc/cni/net.d/10-flannel.conflist
           volumeMounts:
           - name: cni
             mountPath: /etc/cni/net.d
           - name: flannel-cfg
             mountPath: /etc/kube-flannel/
         containers:
         - name: kube-flannel
           image: quay.io/coreos/flannel:v0.12.0-arm64
           command:
           - /opt/bin/flanneld
           args:
           - --ip-masq
           - --kube-subnet-mgr
           resources:
             requests:
               cpu: "100m"
               memory: "50Mi"
             limits:
               cpu: "100m"
               memory: "50Mi"
           securityContext:
             privileged: false
             capabilities:
                add: ["NET_ADMIN"]
           env:
           - name: POD_NAME
             valueFrom:
               fieldRef:
                 fieldPath: metadata.name
           - name: POD_NAMESPACE
             valueFrom:
               fieldRef:
                 fieldPath: metadata.namespace
           volumeMounts:
           - name: run
             mountPath: /run/flannel
           - name: flannel-cfg
             mountPath: /etc/kube-flannel/
         volumes:
           - name: run
             hostPath:
               path: /run/flannel
           - name: cni
             hostPath:
               path: /etc/cni/net.d
           - name: flannel-cfg
             configMap:
               name: kube-flannel-cfg
   ---
   apiVersion: apps/v1
   kind: DaemonSet
   metadata:
     name: kube-flannel-ds-arm
     namespace: kube-system
     labels:
       tier: node
       app: flannel
   spec:
     selector:
       matchLabels:
         app: flannel
     template:
       metadata:
         labels:
           tier: node
           app: flannel
       spec:
         affinity:
           nodeAffinity:
             requiredDuringSchedulingIgnoredDuringExecution:
               nodeSelectorTerms:
                 - matchExpressions:
                     - key: beta.kubernetes.io/os
                       operator: In
                       values:
                         - linux
                     - key: beta.kubernetes.io/arch
                       operator: In
                       values:
                         - arm
         hostNetwork: true
         tolerations:
         - operator: Exists
           effect: NoSchedule
         serviceAccountName: flannel
         initContainers:
         - name: install-cni
           image: quay.io/coreos/flannel:v0.12.0-arm
           command:
           - cp
           args:
           - -f
           - /etc/kube-flannel/cni-conf.json
           - /etc/cni/net.d/10-flannel.conflist
           volumeMounts:
           - name: cni
             mountPath: /etc/cni/net.d
           - name: flannel-cfg
             mountPath: /etc/kube-flannel/
         containers:
         - name: kube-flannel
           image: quay.io/coreos/flannel:v0.12.0-arm
           command:
           - /opt/bin/flanneld
           args:
           - --ip-masq
           - --kube-subnet-mgr
           resources:
             requests:
               cpu: "100m"
               memory: "50Mi"
             limits:
               cpu: "100m"
               memory: "50Mi"
           securityContext:
             privileged: false
             capabilities:
                add: ["NET_ADMIN"]
           env:
           - name: POD_NAME
             valueFrom:
               fieldRef:
                 fieldPath: metadata.name
           - name: POD_NAMESPACE
             valueFrom:
               fieldRef:
                 fieldPath: metadata.namespace
           volumeMounts:
           - name: run
             mountPath: /run/flannel
           - name: flannel-cfg
             mountPath: /etc/kube-flannel/
         volumes:
           - name: run
             hostPath:
               path: /run/flannel
           - name: cni
             hostPath:
               path: /etc/cni/net.d
           - name: flannel-cfg
             configMap:
               name: kube-flannel-cfg
   ---
   apiVersion: apps/v1
   kind: DaemonSet
   metadata:
     name: kube-flannel-ds-ppc64le
     namespace: kube-system
     labels:
       tier: node
       app: flannel
   spec:
     selector:
       matchLabels:
         app: flannel
     template:
       metadata:
         labels:
           tier: node
           app: flannel
       spec:
         affinity:
           nodeAffinity:
             requiredDuringSchedulingIgnoredDuringExecution:
               nodeSelectorTerms:
                 - matchExpressions:
                     - key: beta.kubernetes.io/os
                       operator: In
                       values:
                         - linux
                     - key: beta.kubernetes.io/arch
                       operator: In
                       values:
                         - ppc64le
         hostNetwork: true
         tolerations:
         - operator: Exists
           effect: NoSchedule
         serviceAccountName: flannel
         initContainers:
         - name: install-cni
           image: quay.io/coreos/flannel:v0.12.0-ppc64le
           command:
           - cp
           args:
           - -f
           - /etc/kube-flannel/cni-conf.json
           - /etc/cni/net.d/10-flannel.conflist
           volumeMounts:
           - name: cni
             mountPath: /etc/cni/net.d
           - name: flannel-cfg
             mountPath: /etc/kube-flannel/
         containers:
         - name: kube-flannel
           image: quay.io/coreos/flannel:v0.12.0-ppc64le
           command:
           - /opt/bin/flanneld
           args:
           - --ip-masq
           - --kube-subnet-mgr
           resources:
             requests:
               cpu: "100m"
               memory: "50Mi"
             limits:
               cpu: "100m"
               memory: "50Mi"
           securityContext:
             privileged: false
             capabilities:
                add: ["NET_ADMIN"]
           env:
           - name: POD_NAME
             valueFrom:
               fieldRef:
                 fieldPath: metadata.name
           - name: POD_NAMESPACE
             valueFrom:
               fieldRef:
                 fieldPath: metadata.namespace
           volumeMounts:
           - name: run
             mountPath: /run/flannel
           - name: flannel-cfg
             mountPath: /etc/kube-flannel/
         volumes:
           - name: run
             hostPath:
               path: /run/flannel
           - name: cni
             hostPath:
               path: /etc/cni/net.d
           - name: flannel-cfg
             configMap:
               name: kube-flannel-cfg
   ---
   apiVersion: apps/v1
   kind: DaemonSet
   metadata:
     name: kube-flannel-ds-s390x
     namespace: kube-system
     labels:
       tier: node
       app: flannel
   spec:
     selector:
       matchLabels:
         app: flannel
     template:
       metadata:
         labels:
           tier: node
           app: flannel
       spec:
         affinity:
           nodeAffinity:
             requiredDuringSchedulingIgnoredDuringExecution:
               nodeSelectorTerms:
                 - matchExpressions:
                     - key: beta.kubernetes.io/os
                       operator: In
                       values:
                         - linux
                     - key: beta.kubernetes.io/arch
                       operator: In
                       values:
                         - s390x
         hostNetwork: true
         tolerations:
         - operator: Exists
           effect: NoSchedule
         serviceAccountName: flannel
         initContainers:
         - name: install-cni
           image: quay.io/coreos/flannel:v0.12.0-s390x
           command:
           - cp
           args:
           - -f
           - /etc/kube-flannel/cni-conf.json
           - /etc/cni/net.d/10-flannel.conflist
           volumeMounts:
           - name: cni
             mountPath: /etc/cni/net.d
           - name: flannel-cfg
             mountPath: /etc/kube-flannel/
         containers:
         - name: kube-flannel
           image: quay.io/coreos/flannel:v0.12.0-s390x
           command:
           - /opt/bin/flanneld
           args:
           - --ip-masq
           - --kube-subnet-mgr
           resources:
             requests:
               cpu: "100m"
               memory: "50Mi"
             limits:
               cpu: "100m"
               memory: "50Mi"
           securityContext:
             privileged: false
             capabilities:
                add: ["NET_ADMIN"]
           env:
           - name: POD_NAME
             valueFrom:
               fieldRef:
                 fieldPath: metadata.name
           - name: POD_NAMESPACE
             valueFrom:
               fieldRef:
                 fieldPath: metadata.namespace
           volumeMounts:
           - name: run
             mountPath: /run/flannel
           - name: flannel-cfg
             mountPath: /etc/kube-flannel/
         volumes:
           - name: run
             hostPath:
               path: /run/flannel
           - name: cni
             hostPath:
               path: /etc/cni/net.d
           - name: flannel-cfg
             configMap:
               name: kube-flannel-cfg
   ```

   

   2. 执行

      ```sh
      kubectl apply -f kube-flannel.yml
      ```

      样例：

      ![1586857609780](C:/Users/yanggeng/Desktop/assets/1586857609780.png)

   3. 查看flannel状态

      ```sh
      kubectl get pod -n kube-system -o wide
      
      kubectl get svc -n kube-system
      ```

   4. 查看node

      ```sh
      kubectl get node
      
      
      ```

      ![1586857828912](C:/Users/yanggeng/Desktop/assets/1586857828912.png)

 # 测试集群正确性

   ## 在lab1上

1. 部署nginx

   ```sh
   kubectl create deployment nginx --image=nginx:alpine
   ```

2. 暴露nginx,两条命令。

   ```sh
   kubectl expose deployment nginx --name=nginx-service --port=80 --target-port=80
   
   kubectl expose deployment nginx --type=NodePort --name=nginx-service-nodeport --port=80 --target-port=80
   ```

3. 查看nginx状态

   ```sh
   [vagrant@lab1 ~]$ kubectl get deploy
   NAME    READY   UP-TO-DATE   AVAILABLE   AGE
   nginx   1/1     1            1           2m40s
   [vagrant@lab1 ~]$ 
   
   [vagrant@lab1 ~]$ kubectl get pod
   NAME                     READY   STATUS    RESTARTS   AGE
   nginx-745b4df97d-gf68w   1/1     Running   0          3m11s
   [vagrant@lab1 ~]$ 
   
   [vagrant@lab1 ~]$ kubectl get svc
   NAME                     TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)        AGE
   kubernetes               ClusterIP   10.96.0.1       <none>        443/TCP        64m
   nginx-service            ClusterIP   10.108.97.60    <none>        80/TCP         2m54s
   nginx-service-nodeport   NodePort    10.105.41.155   <none>        80:31728/TCP   105s
   [vagrant@lab1 ~]$ 
   
   ```

4. 通过pod访问nginx。

   ```sh
   [vagrant@lab1 ~]$ kubectl get pod -o wide
   NAME                     READY   STATUS    RESTARTS   AGE     IP           NODE   NOMINATED NODE   READINESS GATES
   nginx-745b4df97d-gf68w   1/1     Running   0          4m30s   10.244.2.2   lab3   <none>           <none>
   [vagrant@lab1 ~]$ curl -I http://10.244.2.2/
   HTTP/1.1 200 OK
   Server: nginx/1.17.9
   Date: Tue, 14 Apr 2020 09:57:27 GMT
   Content-Type: text/html
   Content-Length: 612
   Last-Modified: Tue, 03 Mar 2020 17:36:53 GMT
   Connection: keep-alive
   ETag: "5e5e95b5-264"
   Accept-Ranges: bytes
   
   [vagrant@lab1 ~]$ 
   
   
   注意ip要一直。
   ```

5. 通过Cluster IP访问

   ```sh
   [vagrant@lab1 ~]$ kubectl get svc nginx-service
   NAME            TYPE        CLUSTER-IP     EXTERNAL-IP   PORT(S)   AGE
   nginx-service   ClusterIP   10.108.97.60   <none>        80/TCP    5m19s
   [vagrant@lab1 ~]$ curl -I http://10.108.97.60/
   HTTP/1.1 200 OK
   Server: nginx/1.17.9
   Date: Tue, 14 Apr 2020 09:59:58 GMT
   Content-Type: text/html
   Content-Length: 612
   Last-Modified: Tue, 03 Mar 2020 17:36:53 GMT
   Connection: keep-alive
   ETag: "5e5e95b5-264"
   Accept-Ranges: bytes
   
   [vagrant@lab1 ~]$ 
   ```

6. 通过NodePort访问Nginx应用。

   ```sh
   [vagrant@lab1 ~]$ kubectl get svc nginx-service-nodeport
   NAME                     TYPE       CLUSTER-IP      EXTERNAL-IP   PORT(S)        AGE
   nginx-service-nodeport   NodePort   10.105.41.155   <none>        80:31728/TCP   7m29s
   [vagrant@lab1 ~]$ curl -I http://lab1:31728/
   HTTP/1.1 200 OK
   Server: nginx/1.17.9
   Date: Tue, 14 Apr 2020 10:03:14 GMT
   Content-Type: text/html
   Content-Length: 612
   Last-Modified: Tue, 03 Mar 2020 17:36:53 GMT
   Connection: keep-alive
   ETag: "5e5e95b5-264"
   Accept-Ranges: bytes
   
   [vagrant@lab1 ~]$ curl -I http://lab2:31728/
   HTTP/1.1 200 OK
   Server: nginx/1.17.9
   Date: Tue, 14 Apr 2020 10:04:31 GMT
   Content-Type: text/html
   Content-Length: 612
   Last-Modified: Tue, 03 Mar 2020 17:36:53 GMT
   Connection: keep-alive
   ETag: "5e5e95b5-264"
   Accept-Ranges: bytes
   
   [vagrant@lab1 ~]$ curl -I http://lab3:31728/
   HTTP/1.1 200 OK
   Server: nginx/1.17.9
   Date: Tue, 14 Apr 2020 10:04:37 GMT
   Content-Type: text/html
   Content-Length: 612
   Last-Modified: Tue, 03 Mar 2020 17:36:53 GMT
   Connection: keep-alive
   ETag: "5e5e95b5-264"
   Accept-Ranges: bytes
   
   [vagrant@lab1 ~]$ 
   ```

7. DNS测试

   ```sh
   [vagrant@lab1 ~]$ cat >dns-test.yaml<<EOF
   > apiVersion: v1
   > kind: Pod
   > metadata:
   >   name: dns-test
   > spec:
   >   containers:
   >   - image: radial/busyboxplus:curl
   >     name: dns-test
   >     stdin: true
   >     tty: true
   >     resources:
   >       requests:
   >         cpu: 50m
   >         memory: 50Mi
   >       limits:
   >         cpu: 100m
   >         memory: 100Mi
   > EOF
   [vagrant@lab1 ~]$ ll
   total 20
   -rw-rw-r--. 1 vagrant vagrant   279 Apr 14 18:11 dns-test.yaml
   -rw-rw-r--. 1 vagrant vagrant 14439 Apr 14 17:44 kube-flannel.yml
   [vagrant@lab1 ~]$ kubectl get pod dns-test
   Error from server (NotFound): pods "dns-test" not found
   [vagrant@lab1 ~]$ kubectl apply -f dns-test.yaml
   pod/dns-test created
   [vagrant@lab1 ~]$ kubectl get pod dns-test
   NAME       READY   STATUS              RESTARTS   AGE
   dns-test   0/1     ContainerCreating   0          2s
   [vagrant@lab1 ~]$ 
   ```



​		测试：

```sh
[vagrant@lab1 ~]$ kubectl exec dns-test -- nslookup kubernetes
Server:    10.96.0.10
Address 1: 10.96.0.10 kube-dns.kube-system.svc.cluster.local

Name:      kubernetes
Address 1: 10.96.0.1 kubernetes.default.svc.cluster.local
[vagrant@lab1 ~]$ kubectl exec dns-test -- nslookup nginx-service
Server:    10.96.0.10
Address 1: 10.96.0.10 kube-dns.kube-system.svc.cluster.local

Name:      nginx-service
Address 1: 10.108.97.60 nginx-service.default.svc.cluster.local
[vagrant@lab1 ~]$ kubectl exec dns-test -- nslookup www.baidu.com
Server:    10.96.0.10
Address 1: 10.96.0.10 kube-dns.kube-system.svc.cluster.local

Name:      www.baidu.com
Address 1: 2408:80f0:410c:1d:0:ff:b07a:39af
Address 2: 61.135.169.125
Address 3: 61.135.169.121
[vagrant@lab1 ~]$ 



通过dns访问
[vagrant@lab1 ~]$ kubectl exec dns-test -- curl -s -I http://nginx-service/
HTTP/1.1 200 OK
Server: nginx/1.17.9
Date: Tue, 14 Apr 2020 10:16:56 GMT
Content-Type: text/html
Content-Length: 612
Last-Modified: Tue, 03 Mar 2020 17:36:53 GMT
Connection: keep-alive
ETag: "5e5e95b5-264"
Accept-Ranges: bytes

[vagrant@lab1 ~]$ 
```



8. 清理

   ```h
   kubectl delete service nginx-service-nodeport
   kubectl delete service nginx-service
   kubectl delete deployment nginx
   kubectl delete -f dns-test.yaml
   
   
   执行如下：
   [vagrant@lab1 ~]$ clear
   [vagrant@lab1 ~]$ kubectl get svc
   NAME                     TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)        AGE
   kubernetes               ClusterIP   10.96.0.1       <none>        443/TCP        86m
   nginx-service            ClusterIP   10.108.97.60    <none>        80/TCP         25m
   nginx-service-nodeport   NodePort    10.105.41.155   <none>        80:31728/TCP   24m
   [vagrant@lab1 ~]$ kubectl delete service nginx-service-nodeport
   service "nginx-service-nodeport" deleted
   [vagrant@lab1 ~]$ kubectl get svc
   NAME            TYPE        CLUSTER-IP     EXTERNAL-IP   PORT(S)   AGE
   kubernetes      ClusterIP   10.96.0.1      <none>        443/TCP   87m
   nginx-service   ClusterIP   10.108.97.60   <none>        80/TCP    25m
   [vagrant@lab1 ~]$ kubectl delete service nginx-service
   service "nginx-service" deleted
   [vagrant@lab1 ~]$ kubectl delete -f dns-test.yaml
   pod "dns-test" deleted
   ^C
   [vagrant@lab1 ~]$ kubectl delete deployment nginx
   deployment.apps "nginx" deleted
   [vagrant@lab1 ~]$ kubectl delete -f dns-test.yaml
   pod "dns-test" deleted
   [vagrant@lab1 ~]$ kubectl get svc
   NAME         TYPE        CLUSTER-IP   EXTERNAL-IP   PORT(S)   AGE
   kubernetes   ClusterIP   10.96.0.1    <none>        443/TCP   88m
   [vagrant@lab1 ~]$ kubectl get node
   NAME   STATUS   ROLES    AGE   VERSION
   lab1   Ready    master   88m   v1.18.1
   lab2   Ready    <none>   50m   v1.18.1
   lab3   Ready    <none>   48m   v1.18.1
   ```

   

​		

# dashboard

安装齐老师方法。

注意，每个机器上都要装 dashboard的镜像。







# dashboard安装服务

修改docker镜像服务器，去阿里云容器服务中:容器镜像服务-》镜像加速器。

在node1和node2上分别执行。

```sh
sudo mkdir -p /etc/docker
sudo tee /etc/docker/daemon.json <<-'EOF'
{
  "registry-mirrors": ["https://5dbdvt41.mirror.aliyuncs.com"]
}
EOF
sudo systemctl daemon-reload
sudo systemctl restart docker
```









# 重启服务器恢复k8s

![1587093976282](C:/Users/yanggeng/Desktop/assets/1587093976282.png)





# 部署dashboard

```sh
wget http://pencil-file.oss-cn-hangzhou.aliyuncs.com/blog/kubernetes-dashboard.yaml
```



kubernetes-dashboard.yaml

```sh
# Copyright 2017 The Kubernetes Authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# ------------------- Dashboard Secret ------------------- #

apiVersion: v1
kind: Secret
metadata:
  labels:
    k8s-app: kubernetes-dashboard
  name: kubernetes-dashboard-certs
  namespace: kube-system
type: Opaque

---
# ------------------- Dashboard Service Account ------------------- #

apiVersion: v1
kind: ServiceAccount
metadata:
  labels:
    k8s-app: kubernetes-dashboard
  name: kubernetes-dashboard
  namespace: kube-system

---
# ------------------- Dashboard Role & Role Binding ------------------- #

kind: Role
apiVersion: rbac.authorization.k8s.io/v1
metadata:
  name: kubernetes-dashboard-minimal
  namespace: kube-system
rules:
  # Allow Dashboard to create 'kubernetes-dashboard-key-holder' secret.
- apiGroups: [""]
  resources: ["secrets"]
  verbs: ["create"]
  # Allow Dashboard to create 'kubernetes-dashboard-settings' config map.
- apiGroups: [""]
  resources: ["configmaps"]
  verbs: ["create"]
  # Allow Dashboard to get, update and delete Dashboard exclusive secrets.
- apiGroups: [""]
  resources: ["secrets"]
  resourceNames: ["kubernetes-dashboard-key-holder", "kubernetes-dashboard-certs"]
  verbs: ["get", "update", "delete"]
  # Allow Dashboard to get and update 'kubernetes-dashboard-settings' config map.
- apiGroups: [""]
  resources: ["configmaps"]
  resourceNames: ["kubernetes-dashboard-settings"]
  verbs: ["get", "update"]
  # Allow Dashboard to get metrics from heapster.
- apiGroups: [""]
  resources: ["services"]
  resourceNames: ["heapster"]
  verbs: ["proxy"]
- apiGroups: [""]
  resources: ["services/proxy"]
  resourceNames: ["heapster", "http:heapster:", "https:heapster:"]
  verbs: ["get"]

---
apiVersion: rbac.authorization.k8s.io/v1
kind: RoleBinding
metadata:
  name: kubernetes-dashboard-minimal
  namespace: kube-system
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: Role
  name: kubernetes-dashboard-minimal
subjects:
- kind: ServiceAccount
  name: kubernetes-dashboard
  namespace: kube-system

---
# ------------------- Dashboard Deployment ------------------- #

kind: Deployment
apiVersion: apps/v1
metadata:
  labels:
    k8s-app: kubernetes-dashboard
  name: kubernetes-dashboard
  namespace: kube-system
spec:
  replicas: 1
  revisionHistoryLimit: 10
  selector:
    matchLabels:
      k8s-app: kubernetes-dashboard
  template:
    metadata:
      labels:
        k8s-app: kubernetes-dashboard
    spec:
      containers:
      - name: kubernetes-dashboard
        image: k8s.gcr.io/kubernetes-dashboard-amd64:v1.10.1
        ports:
        - containerPort: 8443
          protocol: TCP
        args:
          - --auto-generate-certificates
          # Uncomment the following line to manually specify Kubernetes API server Host
          # If not specified, Dashboard will attempt to auto discover the API server and connect
          # to it. Uncomment only if the default does not work.
          # - --apiserver-host=http://my-address:port
        volumeMounts:
        - name: kubernetes-dashboard-certs
          mountPath: /certs
          # Create on-disk volume to store exec logs
        - mountPath: /tmp
          name: tmp-volume
        livenessProbe:
          httpGet:
            scheme: HTTPS
            path: /
            port: 8443
          initialDelaySeconds: 30
          timeoutSeconds: 30
      volumes:
      - name: kubernetes-dashboard-certs
        secret:
          secretName: kubernetes-dashboard-certs
      - name: tmp-volume
        emptyDir: {}
      serviceAccountName: kubernetes-dashboard
      # Comment the following tolerations if Dashboard must not be deployed on master
      tolerations:
      - key: node-role.kubernetes.io/master
        effect: NoSchedule

---
# ------------------- Dashboard Service ------------------- #

kind: Service
apiVersion: v1
metadata:
  labels:
    k8s-app: kubernetes-dashboard
  name: kubernetes-dashboard
  namespace: kube-system
spec:
  type: NodePort
  ports:
    - port: 443
      targetPort: 8443
  selector:
    k8s-app: kubernetes-dashboard

```



```sh
kubectl create -f kubernetes-dashboard.yaml
```



dashboard-adminuser.yaml

```sh
---
apiVersion: v1
kind: ServiceAccount
metadata:
  labels:
    k8s-app: kubernetes-dashboard
  name: kubernetes-dashboard-admin
  namespace: kube-system

---
apiVersion: rbac.authorization.k8s.io/v1beta1
kind: ClusterRoleBinding
metadata:
  name: kubernetes-dashboard-admin
  labels:
    k8s-app: kubernetes-dashboard
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: ClusterRole
  name: cluster-admin
subjects:
- kind: ServiceAccount
  name: kubernetes-dashboard-admin
  namespace: kube-system
```



```sh
kubectl apply -f dashboard-adminuser.yaml
```



查看token

```sh
[vagrant@lab1 ~]$ kubectl -n kube-system describe secret $(kubectl -n kube-system get secret | grep kubernetes-dashboard-admin-token | awk '{print $1}')
Name:         kubernetes-dashboard-admin-token-22bfl
Namespace:    kube-system
Labels:       <none>
Annotations:  kubernetes.io/service-account.name: kubernetes-dashboard-admin
              kubernetes.io/service-account.uid: 00398169-042e-41d5-9179-326f13ed0174

Type:  kubernetes.io/service-account-token

Data
====
ca.crt:     1025 bytes
namespace:  11 bytes
token:      eyJhbGciOiJSUzI1NiIsImtpZCI6ImE3V2FSVFVXM3ZNQTVGVFVVeUdmWEwtejVlRWRZTXBPMnlUZVMyQnoxRmMifQ.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJrdWJlLXN5c3RlbSIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VjcmV0Lm5hbWUiOiJrdWJlcm5ldGVzLWRhc2hib2FyZC1hZG1pbi10b2tlbi0yMmJmbCIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50Lm5hbWUiOiJrdWJlcm5ldGVzLWRhc2hib2FyZC1hZG1pbiIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VydmljZS1hY2NvdW50LnVpZCI6IjAwMzk4MTY5LTA0MmUtNDFkNS05MTc5LTMyNmYxM2VkMDE3NCIsInN1YiI6InN5c3RlbTpzZXJ2aWNlYWNjb3VudDprdWJlLXN5c3RlbTprdWJlcm5ldGVzLWRhc2hib2FyZC1hZG1pbiJ9.hFjWhnZMDmUZHYNaNJmwRCNpv8Xc6AmMGMqXsUKZH01Cfc409worbgcRwJ3g8dWwnE4n8o7m0wN3ZLnYBDKTejn5zWSM8NMA8tiQxyFZpEey1UbiUI1r5HDPKOhEW5Zzms1Oq50B6PfCOdJtsvb5z__KNrnWE_HygFBnkt3QkC-wyZKQzBbcxUkaxSZvNJiPyttVSOqZdnsMkGYEdWQpdDUDVJfBBzWy7MXdsGs1mgh3RvGPEPFvWBGNsy6VvTY5mjLEpSCnBpoGfa9sFn0aySjRyHq8kmKTAHhkeFqNymqBw-nnlLu271TsxlzKpWANOIKz3PkieNGwrbhcqGAmhA
[vagrant@lab1 ~]$ 
```



查看端口：

```sh
[vagrant@lab1 ~]$ kubectl get svc -n kube-system
NAME                   TYPE        CLUSTER-IP       EXTERNAL-IP   PORT(S)                  AGE
kube-dns               ClusterIP   10.96.0.10       <none>        53/UDP,53/TCP,9153/TCP   2d22h
kubernetes-dashboard   NodePort    10.109.161.183   <none>        443:31654/TCP            17m
[vagrant@lab1 ~]$ 
```





k8s.gcr.io/kubernetes-dashboard-amd64:v1.10.1











# 扩展

```sh
kubectl get pods

kubectl get pod --all-namespaces
```



重启机器后：

```sh
kubectl get pod --all-namespaces
```



查看加入master节点的命令

```sh

```



kubectl apply -f 的逆操作：kubectl delete -f rc-nginx.yaml



以上是正确步骤。

---



# istio

学习的实验环境：

```sh
https://www.katacoda.com/courses/istio
```











   

   

   

   

   

   

   

   

   

   

   

   



   































































---

一下是学习步骤：作废。可以参考一下 额外的命令进行学习。

*** 步骤 ***

1. 导入已经下载的box

   ```sh
   切换到vagrant目录：
   cd /c/vagrant
   
   
   导入box
   $ vagrant.exe box add --name centos-7.4-base /c/vagrant/centos-7.4-base.box
   ==> box: Box file was not detected as metadata. Adding it directly...
   ==> box: Adding box 'centos-7.4-base' (v0) for provider:
       box: Unpacking necessary files from: file:///C:/vagrant/centos-7.4-base.box
       box:
   ==> box: Successfully added box 'centos-7.4-base' (v0) for 'virtualbox'!
   
   cpf@LAPTOP-BH5NFMO1 MINGW64 /c/vagrant
   
   
   ```

2. 查看box

   ```sh
   $ vagrant.exe box list
   centos-7.4-base (virtualbox, 0)
   
   cpf@LAPTOP-BH5NFMO1 MINGW64 /c/vagrant
   
   
   ```

3. 删除box

   ```sh
   cpf@LAPTOP-BH5NFMO1 MINGW64 /c/istio
   $ vagrant.exe box remove centos-7.4-base
   Removing box 'centos-7.4-base' (v0) with provider 'virtualbox'...
   
   ```

4. 初始化

   ```sh
   cpf@LAPTOP-BH5NFMO1 MINGW64 /c/istio
   $ mkdir test
   
   cpf@LAPTOP-BH5NFMO1 MINGW64 /c/istio
   $ cd test/
   
   cpf@LAPTOP-BH5NFMO1 MINGW64 /c/istio/test
   $ vagrant.exe init centos-7.4-base
   A `Vagrantfile` has been placed in this directory. You are now
   ready to `vagrant up` your first virtual environment! Please read
   the comments in the Vagrantfile as well as documentation on
   `vagrantup.com` for more information on using Vagrant.
   
   ```

5. 启动虚拟机

   ```sh
   cpf@LAPTOP-BH5NFMO1 MINGW64 /c/istio/test
   $ vagrant.exe up
   Bringing machine 'default' up with 'virtualbox' provider...
   ==> default: Importing base box 'centos-7.4-base'...
   ==> default: Matching MAC address for NAT networking...
   ==> default: Setting the name of the VM: test_default_1586658527136_70914
   ==> default: Clearing any previously set network interfaces...
   ==> default: Preparing network interfaces based on configuration...
       default: Adapter 1: nat
   ==> default: Forwarding ports...
       default: 22 (guest) => 2222 (host) (adapter 1)
   ==> default: Booting VM...
   ==> default: Waiting for machine to boot. This may take a few minutes...
       default: SSH address: 127.0.0.1:2222  #关键信息
       default: SSH username: vagrant	#关键信息
       default: SSH auth method: private key
       default:
       default: Vagrant insecure key detected. Vagrant will automatically replace
       default: this with a newly generated keypair for better security.
       default:
       default: Inserting generated public key within guest...
       default: Removing insecure key from the guest if it's present...
       default: Key inserted! Disconnecting and reconnecting using new SSH key...
   ==> default: Machine booted and ready!#关键信息
   ==> default: Checking for guest additions in VM...
       default: No guest additions were detected on the base box for this VM! Guest
       default: additions are required for forwarded ports, shared folders, host only
       default: networking, and more. If SSH fails on this machine, please install
       default: the guest additions and repackage the box to continue.
       default:
       default: This is not an error message; everything may continue to work properly,#关键信息
       default: in which case you may ignore this message.
   
   cpf@LAPTOP-BH5NFMO1 MINGW64 /c/istio/test
   ```

6. 查看虚拟机状态

   ```sh
   $ vagrant.exe status
   Current machine states:
   
   default                   running (virtualbox)
   
   The VM is running. To stop this VM, you can run `vagrant halt` to
   shut it down forcefully, or you can run `vagrant suspend` to simply
   suspend the virtual machine. In either case, to restart it again,
   simply run `vagrant up`.
   
   ```

7. 连接

   ```sh
   $ vagrant.exe ssh
   Last login: Tue Dec 12 16:01:23 2017 from 10.0.2.2
   [vagrant@localhost ~]$
   
   
   
   在系统中其他操作
   $ vagrant.exe ssh
   Last login: Tue Dec 12 16:01:23 2017 from 10.0.2.2
   [vagrant@localhost ~]$ ll
   total 0
   [vagrant@localhost ~]$ cd /
   [vagrant@localhost /]$ ll
   total 16
   lrwxrwxrwx.  1 root root    7 Oct 28  2017 bin -> usr/bin
   dr-xr-xr-x.  5 root root 4096 Oct 28  2017 boot
   drwxr-xr-x. 18 root root 2960 Apr 12 10:29 dev
   drwxr-xr-x. 78 root root 8192 Apr 12 10:29 etc
   drwxr-xr-x.  3 root root   21 Oct 28  2017 home
   lrwxrwxrwx.  1 root root    7 Oct 28  2017 lib -> usr/lib
   lrwxrwxrwx.  1 root root    9 Oct 28  2017 lib64 -> usr/lib64
   drwxr-xr-x.  2 root root    6 Nov  5  2016 media
   drwxr-xr-x.  2 root root    6 Nov  5  2016 mnt
   drwxr-xr-x.  2 root root    6 Nov  5  2016 opt
   dr-xr-xr-x. 99 root root    0 Apr 12 10:29 proc
   dr-xr-x---.  2 root root  158 Dec 12  2017 root
   drwxr-xr-x. 25 root root  800 Apr 12 10:29 run
   lrwxrwxrwx.  1 root root    8 Oct 28  2017 sbin -> usr/sbin
   drwxr-xr-x.  2 root root    6 Nov  5  2016 srv
   dr-xr-xr-x. 13 root root    0 Apr 12 10:28 sys
   drwxrwxrwt.  8 root root  172 Apr 12 10:29 tmp
   drwxr-xr-x. 13 root root  155 Oct 28  2017 usr
   drwxr-xr-x. 18 root root  254 Dec 12  2017 var
   [vagrant@localhost /]$ hostname
   localhost
   [vagrant@localhost /]$ ip a
   1: lo: <LOOPBACK,UP,LOWER_UP> mtu 65536 qdisc noqueue state UNKNOWN qlen 1
       link/loopback 00:00:00:00:00:00 brd 00:00:00:00:00:00
       inet 127.0.0.1/8 scope host lo
          valid_lft forever preferred_lft forever
       inet6 ::1/128 scope host
          valid_lft forever preferred_lft forever
   2: eth0: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc pfifo_fast state UP qlen 1000
       link/ether 52:54:00:ca:e4:8b brd ff:ff:ff:ff:ff:ff
       inet 10.0.2.15/24 brd 10.0.2.255 scope global dynamic eth0
          valid_lft 86098sec preferred_lft 86098sec
       inet6 fe80::5054:ff:feca:e48b/64 scope link
          valid_lft forever preferred_lft forever
   [vagrant@localhost /]$ exit
   logout
   Connection to 127.0.0.1 closed.
   
   ```

8. 重载配置

   ```sh
   [vagrant@localhost ~]$ hostname
   localhost
   
   退出系统
   
   在win上修改test下Vagrant
   增加：
     config.vm.box = "centos-7.4-base"
     config.vm.hostname = "istio"
     
   在win上执行：
   $ vagrant.exe reload
   ==> default: Attempting graceful shutdown of VM...
   ==> default: Clearing any previously set forwarded ports...
   ==> default: Clearing any previously set network interfaces...
   ==> default: Preparing network interfaces based on configuration...
       default: Adapter 1: nat
   ==> default: Forwarding ports...
       default: 22 (guest) => 2222 (host) (adapter 1)
   ==> default: Booting VM...
   ==> default: Waiting for machine to boot. This may take a few minutes...
       default: SSH address: 127.0.0.1:2222
       default: SSH username: vagrant
       default: SSH auth method: private key
   ==> default: Machine booted and ready!
   ==> default: Checking for guest additions in VM...
       default: No guest additions were detected on the base box for this VM! Guest
       default: additions are required for forwarded ports, shared folders, host only
       default: networking, and more. If SSH fails on this machine, please install
       default: the guest additions and repackage the box to continue.
       default:
       default: This is not an error message; everything may continue to work properly,
       default: in which case you may ignore this message.
   ==> default: Setting hostname...
   ==> default: Machine already provisioned. Run `vagrant provision` or use the `--provision`
   ==> default: flag to force provisioning. Provisioners marked to run always will still run.
     
     
     
   $ vagrant.exe ssh
   Last login: Sun Apr 12 10:37:30 2020 from 10.0.2.2
   [vagrant@istio ~]$ hostname
   istio
   [vagrant@istio ~]$
   
   
   ```

9. 暂停虚拟机

   ```sh
   cpf@LAPTOP-BH5NFMO1 MINGW64 /c/istio/test
   $ vagrant.exe suspend
   ==> default: Saving VM state and suspending execution...
   
   cpf@LAPTOP-BH5NFMO1 MINGW64 /c/istio/test
   $ vagrant.exe ssh
   VM must be running to open SSH connection. Run `vagrant up`
   to start the virtual machine.
   
   cpf@LAPTOP-BH5NFMO1 MINGW64 /c/istio/test
   $ vagrant.exe status
   Current machine states:
   
   default                   saved (virtualbox)
   
   To resume this VM, simply run `vagrant up`.
   
   cpf@LAPTOP-BH5NFMO1 MINGW64 /c/istio/test
   
   
   唤醒虚拟机
   
   ```

10. 关机

    ```sh
    cpf@LAPTOP-BH5NFMO1 MINGW64 /c/istio/test
    $ vagrant.exe halt
    ==> default: Attempting graceful shutdown of VM...
    
    cpf@LAPTOP-BH5NFMO1 MINGW64 /c/istio/test
    $ vagrant.exe status
    Current machine states:
    
    default                   poweroff (virtualbox)
    
    The VM is powered off. To restart the VM, simply run `vagrant up`
    
    cpf@LAPTOP-BH5NFMO1 MINGW64 /c/istio/test
    
    ```

11. 销毁,(删除虚拟机)

    ```sh
    cpf@LAPTOP-BH5NFMO1 MINGW64 /c/istio/test
    $ vagrant.exe destroy
        default: Are you sure you want to destroy the 'default' VM? [y/N] y
    ==> default: Destroying VM and associated drives...
    
    cpf@LAPTOP-BH5NFMO1 MINGW64 /c/istio/test
    $ vagrant.exe status
    Current machine states:
    
    default                   not created (virtualbox)
    
    The environment has not yet been created. Run `vagrant up` to
    create the environment. If a machine is not created, only the
    default provider will be shown. So if a provider is not listed,
    then the machine is not created for that environment.
    
    ```

12. 快照操作

    ```sh
    查看快照
    $ vagrant.exe snapshot list
    ==> default: No snapshots have been taken yet!
        default: You can take a snapshot using `vagrant snapshot save`. Note that
        default: not all providers support this yet. Once a snapshot is taken, you
        default: can list them using this command, and use commands such as
        default: `vagrant snapshot restore` to go back to a certain snapshot.
        
        
    启动虚拟机
    $ vagrant.exe box list
    centos-7.4-base (virtualbox, 0)
    cpf@LAPTOP-BH5NFMO1 MINGW64 /c/istio/test
    $ vagrant.exe up
    
    
    保存快照
    $ vagrant.exe snapshot save istio
    ==> default: Snapshotting the machine as 'istio'...
    ==> default: Snapshot saved! You can restore the snapshot at any time by
    ==> default: using `vagrant snapshot restore`. You can delete it using
    ==> default: `vagrant snapshot delete`.
    
    
    从快照恢复
    cpf@LAPTOP-BH5NFMO1 MINGW64 /c/istio/test
    $ vagrant.exe snapshot restore istio
    ==> default: Forcing shutdown of VM...
    ==> default: Restoring the snapshot 'istio'...
    ==> default: Resuming suspended VM...
    ==> default: Booting VM...
    ==> default: Waiting for machine to boot. This may take a few minutes...
        default: SSH address: 127.0.0.1:2222
        default: SSH username: vagrant
        default: SSH auth method: private key
    ==> default: Machine booted and ready!
    ==> default: Machine already provisioned. Run `vagrant provision` or use the `--provision`
    ==> default: flag to force provisioning. Provisioners marked to run always will still run.
    
    
    
    删除快照
    $ vagrant.exe snapshot list
    istio
    
    cpf@LAPTOP-BH5NFMO1 MINGW64 /c/istio/test
    $ vagrant.exe snapshot delete istio
    ==> default: Deleting the snapshot 'istio'...
    ==> default: Snapshot deleted!
    
    cpf@LAPTOP-BH5NFMO1 MINGW64 /c/istio/test
    $ vagrant.exe snapshot list
    ==> default: No snapshots have been taken yet!
        default: You can take a snapshot using `vagrant snapshot save`. Note that
        default: not all providers support this yet. Once a snapshot is taken, you
        default: can list them using this command, and use commands such as
        default: `vagrant snapshot restore` to go back to a certain snapshot.
    
    cpf@LAPTOP-BH5NFMO1 MINGW64 /c/istio/test
    
    
    ```

    删除machine ，去vmbox界面删除即可。

    

    

    *** 安装实验用的系统***

    

    创建一个目录：istio，

    创建一个文件Vagrantfile

    ```sh
    Vagrant.configure("2") do |config|
    	# 创建3台虚拟机
    	(1..3).each do |i|
    		config.vm.define "lab#{i}" do |node|
    			# 表示使用名为：centos-7.4-base的  box 
    			node.vm.box = "centos-7.4-base"
    			# 表示不自动生成新的ssh key，使用Vagrant默认的ssh key注入到虚拟机中，这么做主要是为了方便登录。不用为每台虚拟机设置ssh key登录。
    			node.ssh.insert_key=false
    			# 设置虚拟机的主机名，设置3台虚拟机主机名分别为lab1、lab2、lab3
    			node.vm.hostname="lab#{i}"
    			# 设置虚拟机的IP，设置3台虚拟机的私有网络为11.11.11.111、11.11.11.112、11.11.11.113。
    			node.vm.network "private_network", ip: "11.11.11.11#{i}"			
    			# 表示当虚拟机启动完成之后，执行inline中配置的shell命令，此命令用于开机之后的时间同步。
    			node.vm.provision "shell", run:"always",
    				inline:"ntpdate ntp.api.bz"
    			# 表示当虚拟机启动完成之后，执行inline中配置的shell命令，此命令用于输出测试字符串。
    			node.vm.provision "shell", run:"always",
    				inline:"echo hello from lab#{i}"
    			# VirtaulBox相关配置，设置虚拟机的CPU核心数和内存大小，本次实验设置为每台虚拟机2核2G内存，可以根据自己电脑的实际情况适当加大。
    			node.vm.provider "virtualbox" do |v|
    				# 设置虚拟机的CPU个数
    				v.cpus = 1
    				v.name="lab#{i}"
    				
    				v.memory="1024"
    			end
      		end
    	end
    end
    ```

    启动 vagrant up

    ```sh
    cpf@LAPTOP-BH5NFMO1 MINGW64 /c/istio/istio
    $ vagrant.exe up
    Bringing machine 'lab1' up with 'virtualbox' provider...
    Bringing machine 'lab2' up with 'virtualbox' provider...
    Bringing machine 'lab3' up with 'virtualbox' provider...
    ==> lab1: Importing base box 'centos-7.4-base'...
    ==> lab1: Matching MAC address for NAT networking...
    ==> lab1: Setting the name of the VM: lab1
    ==> lab1: Fixed port collision for 22 => 2222. Now on port 2200.
    ==> lab1: Clearing any previously set network interfaces...
    ==> lab1: Preparing network interfaces based on configuration...
        lab1: Adapter 1: nat
        lab1: Adapter 2: hostonly
    ==> lab1: Forwarding ports...
        lab1: 22 (guest) => 2200 (host) (adapter 1)
    ==> lab1: Running 'pre-boot' VM customizations...
    ==> lab1: Booting VM...
    ==> lab1: Waiting for machine to boot. This may take a few minutes...
        lab1: SSH address: 127.0.0.1:2200
        lab1: SSH username: vagrant
        lab1: SSH auth method: private key
    ==> lab1: Machine booted and ready!
    ==> lab1: Checking for guest additions in VM...
        lab1: No guest additions were detected on the base box for this VM! Guest
        lab1: additions are required for forwarded ports, shared folders, host only
        lab1: networking, and more. If SSH fails on this machine, please install
        lab1: the guest additions and repackage the box to continue.
        lab1:
        lab1: This is not an error message; everything may continue to work properly,
        lab1: in which case you may ignore this message.
    ==> lab1: Setting hostname...
    ==> lab1: Configuring and enabling network interfaces...
    ==> lab1: Running provisioner: shell...
        lab1: Running: inline script
        lab1: 12 Apr 12:14:27 ntpdate[2739]: adjust time server 114.118.7.161 offset 0.103360 sec
    ==> lab1: Running provisioner: shell...
        lab1: Running: inline script
        lab1: hello from lab1
    ==> lab2: Importing base box 'centos-7.4-base'...
    ==> lab2: Matching MAC address for NAT networking...
    ==> lab2: Setting the name of the VM: lab2
    ==> lab2: Fixed port collision for 22 => 2222. Now on port 2201.
    ==> lab2: Clearing any previously set network interfaces...
    ==> lab2: Preparing network interfaces based on configuration...
        lab2: Adapter 1: nat
        lab2: Adapter 2: hostonly
    ==> lab2: Forwarding ports...
        lab2: 22 (guest) => 2201 (host) (adapter 1)
    ==> lab2: Running 'pre-boot' VM customizations...
    ==> lab2: Booting VM...
    ==> lab2: Waiting for machine to boot. This may take a few minutes...
        lab2: SSH address: 127.0.0.1:2201
        lab2: SSH username: vagrant
        lab2: SSH auth method: private key
    ==> lab2: Machine booted and ready!
    ==> lab2: Checking for guest additions in VM...
        lab2: No guest additions were detected on the base box for this VM! Guest
        lab2: additions are required for forwarded ports, shared folders, host only
        lab2: networking, and more. If SSH fails on this machine, please install
        lab2: the guest additions and repackage the box to continue.
        lab2:
        lab2: This is not an error message; everything may continue to work properly,
        lab2: in which case you may ignore this message.
    ==> lab2: Setting hostname...
    ==> lab2: Configuring and enabling network interfaces...
    ==> lab2: Running provisioner: shell...
        lab2: Running: inline script
        lab2: 12 Apr 12:15:08 ntpdate[2709]: adjust time server 114.118.7.161 offset 0.010448 sec
    ==> lab2: Running provisioner: shell...
        lab2: Running: inline script
        lab2: hello from lab2
    ==> lab3: Importing base box 'centos-7.4-base'...
    ==> lab3: Matching MAC address for NAT networking...
    ==> lab3: Setting the name of the VM: lab3
    ==> lab3: Fixed port collision for 22 => 2222. Now on port 2202.
    ==> lab3: Clearing any previously set network interfaces...
    ==> lab3: Preparing network interfaces based on configuration...
        lab3: Adapter 1: nat
        lab3: Adapter 2: hostonly
    ==> lab3: Forwarding ports...
        lab3: 22 (guest) => 2202 (host) (adapter 1)
    ==> lab3: Running 'pre-boot' VM customizations...
    ==> lab3: Booting VM...
    ==> lab3: Waiting for machine to boot. This may take a few minutes...
        lab3: SSH address: 127.0.0.1:2202
        lab3: SSH username: vagrant
        lab3: SSH auth method: private key
    ==> lab3: Machine booted and ready!
    ==> lab3: Checking for guest additions in VM...
        lab3: No guest additions were detected on the base box for this VM! Guest
        lab3: additions are required for forwarded ports, shared folders, host only
        lab3: networking, and more. If SSH fails on this machine, please install
        lab3: the guest additions and repackage the box to continue.
        lab3:
        lab3: This is not an error message; everything may continue to work properly,
        lab3: in which case you may ignore this message.
    ==> lab3: Setting hostname...
    ==> lab3: Configuring and enabling network interfaces...
    ==> lab3: Running provisioner: shell...
        lab3: Running: inline script
        lab3: 12 Apr 12:15:49 ntpdate[2740]: adjust time server 114.118.7.163 offset 0.039824 sec
    ==> lab3: Running provisioner: shell...
        lab3: Running: inline script
        lab3: hello from lab3
    
    cpf@LAPTOP-BH5NFMO1 MINGW64 /c/istio/istio
    
    ```

     我

    

13. 连接虚拟机集群

    ```sh
    
    ```

14. 暂停实验环境

    当我们的实验进行到一定步骤后，可能需要暂停，换个时间再次进行实验。这个时候我们可以直接暂停整个实验环境中的所有虚拟机，等下次再进行实验时，直接恢复之前的环境即可，非常方便。

    ```sh
    暂停所有，恢复所有
    $ vagrant.exe suspend
    ==> lab1: Saving VM state and suspending execution...
    ==> lab2: Saving VM state and suspending execution...
    ==> lab3: Saving VM state and suspending execution...
    
    cpf@LAPTOP-BH5NFMO1 MINGW64 /c/istio/istio
    $ vagrant.exe resume
    
    暂停单个，恢复单个
    cpf@LAPTOP-BH5NFMO1 MINGW64 /c/istio/istio
    $ vagrant.exe suspend lab1
    ==> lab1: Saving VM state and suspending execution...
    
    cpf@LAPTOP-BH5NFMO1 MINGW64 /c/istio/istio
    $ vagrant.exe resume lab1
    ==> lab1: Resuming suspended VM...
    ==> lab1: Booting VM...
    ==> lab1: Waiting for machine to boot. This may take a few minutes...
        lab1: SSH address: 127.0.0.1:2200
        lab1: SSH username: vagrant
        lab1: SSH auth method: private key
    ==> lab1: Machine booted and ready!
    ==> lab1: Machine already provisioned. Run `vagrant provision` or use the `--provision`
    ==> lab1: flag to force provisioning. Provisioners marked to run always will still run.
    ==> lab1: Running provisioner: shell...
        lab1: Running: inline script
        lab1: 12 Apr 14:45:55 ntpdate[3664]: step time server 114.118.7.161 offset 26.734348 sec
    ==> lab1: Running provisioner: shell...
        lab1: Running: inline script
        lab1: hello from lab1
    
    ```

15. 保存于恢复实验环境

    完成前面的步骤后，我们的实验虚拟机环境就已经基本搭建完成了。之后我们可以安装Docker、Git、Wget等基础软件。安装之后保存实验环境，之后实验不成功或者实验环境被污染，可以快速恢复到当前的实验环境。

    ```sh
    保存恢复所有
    cpf@LAPTOP-BH5NFMO1 MINGW64 /c/istio/istio
    $ vagrant.exe snapshot save base
    ==> lab1: Snapshotting the machine as 'base'...
    ==> lab1: Snapshot saved! You can restore the snapshot at any time by
    ==> lab1: using `vagrant snapshot restore`. You can delete it using
    ==> lab1: `vagrant snapshot delete`.
    ==> lab2: Snapshotting the machine as 'base'...
    ==> lab2: Snapshot saved! You can restore the snapshot at any time by
    ==> lab2: using `vagrant snapshot restore`. You can delete it using
    ==> lab2: `vagrant snapshot delete`.
    ==> lab3: Snapshotting the machine as 'base'...
    ==> lab3: Snapshot saved! You can restore the snapshot at any time by
    ==> lab3: using `vagrant snapshot restore`. You can delete it using
    ==> lab3: `vagrant snapshot delete`.
    
    cpf@LAPTOP-BH5NFMO1 MINGW64 /c/istio/istio
    $ vagrant.exe snapshot list
    base
    base
    base
    
    
    保存恢复单个
    $ vagrant.exe snapshot save lab1 base1
    ==> lab1: Snapshotting the machine as 'base1'...
    ==> lab1: Snapshot saved! You can restore the snapshot at any time by
    ==> lab1: using `vagrant snapshot restore`. You can delete it using
    ==> lab1: `vagrant snapshot delete`.
    
    cpf@LAPTOP-BH5NFMO1 MINGW64 /c/istio/istio
    $ vagrant.exe snapshot list
    base
    base1
    base
    base
    
    
    删除快照
    cpf@LAPTOP-BH5NFMO1 MINGW64 /c/istio/istio
    $ vagrant.exe snapshot delete base
    ==> lab1: Deleting the snapshot 'base'...
    ==> lab1: Snapshot deleted!
    ==> lab2: Deleting the snapshot 'base'...
    ==> lab2: Snapshot deleted!
    ==> lab3: Deleting the snapshot 'base'...
    ==> lab3: Snapshot deleted!
    
    cpf@LAPTOP-BH5NFMO1 MINGW64 /c/istio/istio
    $ vagrant.exe snapshot list
    base1
    ==> lab2: No snapshots have been taken yet!
        lab2: You can take a snapshot using `vagrant snapshot save`. Note that
        lab2: not all providers support this yet. Once a snapshot is taken, you
        lab2: can list them using this command, and use commands such as
        lab2: `vagrant snapshot restore` to go back to a certain snapshot.
    ==> lab3: No snapshots have been taken yet!
        lab3: You can take a snapshot using `vagrant snapshot save`. Note that
        lab3: not all providers support this yet. Once a snapshot is taken, you
        lab3: can list them using this command, and use commands such as
        lab3: `vagrant snapshot restore` to go back to a certain snapshot.
    
    cpf@LAPTOP-BH5NFMO1 MINGW64 /c/istio/istio
    $ vagrant.exe snapshot delete lab1 base1
    ==> lab1: Deleting the snapshot 'base1'...
    ==> lab1: Snapshot deleted!
    
    cpf@LAPTOP-BH5NFMO1 MINGW64 /c/istio/istio
    $ vagrant.exe snapshot list
    ==> lab1: No snapshots have been taken yet!
        lab1: You can take a snapshot using `vagrant snapshot save`. Note that
        lab1: not all providers support this yet. Once a snapshot is taken, you
        lab1: can list them using this command, and use commands such as
        lab1: `vagrant snapshot restore` to go back to a certain snapshot.
    ==> lab2: No snapshots have been taken yet!
        lab2: You can take a snapshot using `vagrant snapshot save`. Note that
        lab2: not all providers support this yet. Once a snapshot is taken, you
        lab2: can list them using this command, and use commands such as
        lab2: `vagrant snapshot restore` to go back to a certain snapshot.
    ==> lab3: No snapshots have been taken yet!
        lab3: You can take a snapshot using `vagrant snapshot save`. Note that
        lab3: not all providers support this yet. Once a snapshot is taken, you
        lab3: can list them using this command, and use commands such as
        lab3: `vagrant snapshot restore` to go back to a certain snapshot.
    
    cpf@LAPTOP-BH5NFMO1 MINGW64 /c/istio/istio
    
    
    
    查看快照：
    cpf@LAPTOP-BH5NFMO1 MINGW64 /c/istio/istio
    $ vagrant.exe snapshot list
    base1
    ==> lab2: No snapshots have been taken yet!
        lab2: You can take a snapshot using `vagrant snapshot save`. Note that
        lab2: not all providers support this yet. Once a snapshot is taken, you
        lab2: can list them using this command, and use commands such as
        lab2: `vagrant snapshot restore` to go back to a certain snapshot.
    ==> lab3: No snapshots have been taken yet!
        lab3: You can take a snapshot using `vagrant snapshot save`. Note that
        lab3: not all providers support this yet. Once a snapshot is taken, you
        lab3: can list them using this command, and use commands such as
        lab3: `vagrant snapshot restore` to go back to a certain snapshot.
    
    cpf@LAPTOP-BH5NFMO1 MINGW64 /c/istio/istio
    $ vagrant.exe snapshot list lab1
    base1
    
    cpf@LAPTOP-BH5NFMO1 MINGW64 /c/istio/istio
    
    
    
    恢复单个快照：
    $ vagrant.exe snapshot list lab1
    base1
    
    cpf@LAPTOP-BH5NFMO1 MINGW64 /c/istio/istio
    $ vagrant.exe snapshot restore lab1 base1
    
    
    恢复所有
    vagrant.exe snapshot restore base
    
    
    
    ```

 安装docker

1. 设置阿里云镜像

   ```sh
   进入lab1，
   Last login: Sun Apr 12 14:44:50 2020 from 11.11.11.1
   [vagrant@lab1 ~]$ sudo yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
   Loaded plugins: fastestmirror
   adding repo from: http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
   grabbing file http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo to /etc/yum.repos.d/docker-ce.repo
   repo saved to /etc/yum.repos.d/docker-ce.repo
   ```

2. 更新软件源信息

   ```sh
   [vagrant@lab1 ~]$ sudo yum makecache fast
   Loaded plugins: fastestmirror
   base                                                                                                                                                        | 3.6 kB  00:00:00     
   docker-ce-stable                                                                                                                                            | 3.5 kB  00:00:00     
   epel                                                                                                                                                        | 4.7 kB  00:00:00     
   extras                                                                                                                                                      | 2.9 kB  00:00:00     
   updates                                                                                                                                                     | 2.9 kB  00:00:00     
   (1/9): base/7/x86_64/group_gz                                                                                                                               | 165 kB  00:00:00     
   (2/9): docker-ce-stable/x86_64/updateinfo                                                                                                                   |   55 B  00:00:00     
   (3/9): epel/x86_64/group_gz                                                                                                                                 |  95 kB  00:00:00     
   (4/9): epel/x86_64/updateinfo                                                                                                                               | 1.0 MB  00:00:01     
   (5/9): docker-ce-stable/x86_64/primary_db                                                                                                                   |  41 kB  00:00:01     
   (6/9): extras/7/x86_64/primary_db                                                                                                                           | 165 kB  00:00:01     
   (7/9): epel/x86_64/primary_db                                                                                                                               | 6.8 MB  00:00:06     
   (8/9): base/7/x86_64/primary_db                                                                                                                             | 6.0 MB  00:00:10     
   (9/9): updates/7/x86_64/primary_db                                                                                                                          | 7.6 MB  00:00:10     
   Determining fastest mirrors
    * base: mirrors.aliyun.com
    * epel: mirrors.aliyun.com
    * extras: mirrors.aliyun.com
    * updates: mirrors.aliyun.com
   Metadata Cache Created
   ```

3. 查看可用版本

   ```sh
   [vagrant@lab1 ~]$  sudo yum list docker-ce --showduplicates | sort -r
    * updates: mirrors.aliyun.com
   Loading mirror speeds from cached hostfile
   Loaded plugins: fastestmirror
    * extras: mirrors.aliyun.com
    * epel: mirrors.aliyun.com
   docker-ce.x86_64            3:19.03.8-3.el7                     docker-ce-stable
   docker-ce.x86_64            3:19.03.7-3.el7                     docker-ce-stable
   docker-ce.x86_64            3:19.03.6-3.el7                     docker-ce-stable
   docker-ce.x86_64            3:19.03.5-3.el7                     docker-ce-stable
   docker-
   
   ```

4. 安装指定版本Docker CE（Kubernetes 1.12版本官方推荐使用DockerCE 18.06版本）：

   ```sh
   添加安装docker依赖
   [vagrant@lab1 ~]$ sudo yum install -y yum-utils device-mapper-persistent-data lvm2
   
   安装docker CE
   [vagrant@lab1 ~]$ sudo yum install -y docker-ce-18.06.1.ce
   
   
   ```

5. 启动docker ce

   ```sh
   [vagrant@lab1 ~]$ sudo systemctl start docker
   [vagrant@lab1 ~]$ sudo docker ps
   CONTAINER ID        IMAGE               COMMAND             CREATED             STATUS              PORTS               NAMES
   [vagrant@lab1 ~]$ sudo systemctl stop docker
   [vagrant@lab1 ~]$ sudo docker ps
   Cannot connect to the Docker daemon at unix:///var/run/docker.sock. Is the docker daemon running?
   
   启动docker命令，
   sudo systemctl start docker
   
   
   
   ```

6. 运行docker镜像

   ```sh
   [vagrant@lab1 ~]$ sudo docker run --rm alpine echo 'hello cpf'
   docker: Cannot connect to the Docker daemon at unix:///var/run/docker.sock. Is the docker daemon running?.
   See 'docker run --help'.
   [vagrant@lab1 ~]$ sudo docker ps
   Cannot connect to the Docker daemon at unix:///var/run/docker.sock. Is the docker daemon running?
   [vagrant@lab1 ~]$ sudo systemctl start docker
   [vagrant@lab1 ~]$ sudo docker run --rm alpine echo 'hello cpf'
   Unable to find image 'alpine:latest' locally
   latest: Pulling from library/alpine
   aad63a933944: Pull complete 
   Digest: sha256:b276d875eeed9c7d3f1cfa7edb06b22ed22b14219a7d67c52c56612330348239
   Status: Downloaded newer image for alpine:latest
   hello cpf
   
   
   查看docker镜像
   [vagrant@lab1 ~]$ sudo docker images
   REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
   alpine              latest              a187dde48cd2        2 weeks ago         5.6MB
   [vagrant@lab1 ~]$ 
   ```

7. 配置镜像加速

   ```sh
   [vagrant@lab1 ~]$ sudo mkdir -p /etc/docker
   
   sudo tee /etc/docker/daemon.json <<-'EOF'
   {
       "registry-mirrors": [
           "https://1nj0zren.mirror.aliyuncs.com",
           "https://docker.mirrors.ustc.edu.cn",
           "http://f1361db2.m.daocloud.io",
           "https://registry.docker-cn.com"
       ]
   }
   EOF
   
   
   查看配置
   [vagrant@lab1 ~]$ sudo cat /etc/docker/daemon.json
   {
       "registry-mirrors": [
           "https://1nj0zren.mirror.aliyuncs.com",
           "https://docker.mirrors.ustc.edu.cn",
           "http://f1361db2.m.daocloud.io",
           "https://registry.docker-cn.com"
       ]
   }
   [vagrant@lab1 ~]$ 
   
   重载配置
   [vagrant@lab1 ~]$ sudo systemctl daemon-reload
   
   重启docker
   [vagrant@lab1 ~]$ sudo systemctl restart docker
   [vagrant@lab1 ~]$ 
   
   拉取镜像：
   [vagrant@lab1 ~]$ sudo docker pull centos:7
   感觉速度是否有变化？
   ```



安装Kubeadm

Kubeadm用于创建K8S集群。



```sh
sudo tee /etc/yum.repos.d/kubernetes.repo <<-'EOF'
[kubernetes]
name=Kubernetes
baseurl=https://mirrors.aliyun.com/kubernetes/yum/repos/kubernetes-el7-x86_64
enabled=1
gpgcheck=1
repo_gpgcheck=1
gpgkey=https://mirrors.aliyun.com/kubernetes/yum/doc/yum-key.gpg https://mirrors.aliyun.com/kubernetes/yum/doc/rpm-package-key.gpg
EOF


查看镜像
[vagrant@lab1 ~]$ sudo cat /etc/yum.repos.d/kubernetes.repo
[kubernetes]
name=Kubernetes
baseurl=https://mirrors.aliyun.com/kubernetes/yum/repos/kubernetes-el7-x86_64
enabled=1
gpgcheck=1
repo_gpgcheck=1
gpgkey=https://mirrors.aliyun.com/kubernetes/yum/doc/yum-key.gpg https://mirrors.aliyun.com/kubernetes/yum/doc/rpm-package-key.gpg
[vagrant@lab1 ~]$ 
```

1. 安装Kubeadm相关软件：

   ```sh
   #缺少此软件
   注意：--nogpgcheck跳过key验证。下面紧挨着两个命令都是如此。
   [vagrant@lab1 ~]$ sudo yum -y install kubernetes-cni = 0.6.0 --nogpgcheck
   
   安装：kubeadm。
   
   sudo yum install -y kubelet-1.12.4 kubeadm-1.12.4 kubectl-1.12.4 ipvsadm --nogpgcheck
   
   ----
   如果出错：
   sudo yum install -y kubelet-1.12.4 kubeadm-1.12.4 kubectl-1.12.4 ipvsadm kubernetes-cni-0.6.0
   
   
   
   Loaded plugins: fastestmirror
   Loading mirror speeds from cached hostfile
    * base: mirrors.aliyun.com
    * epel: mirrors.aliyun.com
    * extras: mirrors.aliyun.com
    * updates: mirrors.aliyun.com
   Package matching kubelet-1.12.4-0.x86_64 already installed. Checking for update.
   Resolving Dependencies
   --> Running transaction check
   ---> Package ipvsadm.x86_64 0:1.27-7.el7 will be installed
   ---> Package kubeadm.x86_64 0:1.12.4-0 will be installed
   --> Processing Dependency: cri-tools >= 1.11.0 for package: kubeadm-1.12.4-0.x86_64
   ---> Package kubectl.x86_64 0:1.12.4-0 will be installed
   --> Running transaction check
   ---> Package cri-tools.x86_64 0:1.13.0-0 will be installed
   --> Finished Dependency Resolution
   
   Dependencies Resolved
   
   ===================================================================================================================================================================================
    Package                                    Arch                                    Version                                      Repository                                   Size
   ===================================================================================================================================================================================
   Installing:
    ipvsadm                                    x86_64                                  1.27-7.el7                                   base                                         45 k
    kubeadm                                    x86_64                                  1.12.4-0                                     kubernetes                                  7.2 M
    kubectl                                    x86_64                                  1.12.4-0                                     kubernetes                                  7.7 M
   Installing for dependencies:
    cri-tools                                  x86_64                                  1.13.0-0                                     kubernetes                                  5.1 M
   
   Transaction Summary
   ===================================================================================================================================================================================
   Install  3 Packages (+1 Dependent package)
   
   Total download size: 20 M
   Installed size: 95 M
   Downloading packages:
   (1/4): ipvsadm-1.27-7.el7.x86_64.rpm                                                                                                                        |  45 kB  00:00:00     
   (2/4): 14bfe6e75a9efc8eca3f638eb22c7e2ce759c67f95b43b16fae4ebabde1549f3-cri-tools-1.13.0-0.x86_64.rpm                                                       | 5.1 MB  00:00:05     
   (3/4): ef8739a3a637246743ee5238ac929308d7c322c0ad1a8806699af04434e545b3-kubeadm-1.12.4-0.x86_64.rpm                                                         | 7.2 MB  00:00:08     
   (4/4): e1ef6b554386a454ddc0d711b7950e5a95ecd5ea750e9c3aa6eb235e15bc7a80-kubectl-1.12.4-0.x86_64.rpm                                                         | 7.7 MB  00:00:05     
   -----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
   Total                                                                                                                                              1.8 MB/s |  20 MB  00:00:10     
   Running transaction check
   Running transaction test
   Transaction test succeeded
   Running transaction
     Installing : cri-tools-1.13.0-0.x86_64                                                                                                                                       1/4 
     Installing : kubectl-1.12.4-0.x86_64                                                                                                                                         2/4 
     Installing : kubeadm-1.12.4-0.x86_64                                                                                                                                         3/4 
     Installing : ipvsadm-1.27-7.el7.x86_64                                                                                                                                       4/4 
     Verifying  : kubectl-1.12.4-0.x86_64                                                                                                                                         1/4 
     Verifying  : ipvsadm-1.27-7.el7.x86_64                                                                                                                                       2/4 
     Verifying  : cri-tools-1.13.0-0.x86_64                                                                                                                                       3/4 
     Verifying  : kubeadm-1.12.4-0.x86_64                                                                                                                                         4/4 
   
   Installed:
     ipvsadm.x86_64 0:1.27-7.el7                                 kubeadm.x86_64 0:1.12.4-0                                 kubectl.x86_64 0:1.12.4-0                                
   
   Dependency Installed:
     cri-tools.x86_64 0:1.13.0-0                                                                                                                                                      
   
   Complete!
   [vagrant@lab1 ~]$ 
   ```

2. 配置基础环境

   ```sh
   关闭防火墙和SELinux
   [vagrant@lab1 ~]$ sudo systemctl stop firewalld
   [vagrant@lab1 ~]$ sudo systemctl disable firewalld
   [vagrant@lab1 ~]$ 
   
   临时关闭SELinux
   vagrant@lab1 ~]$ sudo setenforce 0
   
   关闭开机启用SELinux
   [vagrant@lab1 ~]$ sudo sed -i 's/SELINUX=permissive/SELINUX=disabled/' /etc/sysconfig/selinux
   
   关闭swap
   查看内存：有swap。
   [vagrant@lab1 ~]$ free -m
                 total        used        free      shared  buff/cache   available
   Mem:            992         140         160          12         691         639
   Swap:          1535           0        1535
   [vagrant@lab1 ~]$ 
   
   关闭临时swap
   [vagrant@lab1 ~]$ sudo swapoff -a
   
   关闭开机自动挂载swap分区
   [vagrant@lab1 ~]$ sudo sed -ri 's@(^/.*swap.*)@#\1@g' /etc/fstab
   [vagrant@lab1 ~]$ 
   
   [vagrant@lab1 ~]$ free -m
                 total        used        free      shared  buff/cache   available
   Mem:            992         139         161          12         691         640
   Swap:             0           0           0
   [vagrant@lab1 ~]$ 
   swap为0 ，表示生效。
   
   
   加载ipvs相关内核模块
   [vagrant@lab1 ~]$ sudo modprobe ip_vs
   [vagrant@lab1 ~]$ sudo modprobe ip_vs_rr
   [vagrant@lab1 ~]$ sudo modprobe ip_vs_wrr
   [vagrant@lab1 ~]$ sudo modprobe ip_vs_sh
   [vagrant@lab1 ~]$ sudo modprobe nf_conntrack_ipv4
   [vagrant@lab1 ~]$ 
   
   查看ipvs相关内存模块，是否导入成功：
   [vagrant@lab1 ~]$ sudo lsmod | grep ip_vs
   ip_vs_sh               12688  0 
   ip_vs_wrr              12697  0 
   ip_vs_rr               12600  0 
   ip_vs                 141092  6 ip_vs_rr,ip_vs_sh,ip_vs_wrr
   nf_conntrack          133387  7 ip_vs,nf_nat,nf_nat_ipv4,xt_conntrack,nf_nat_masquerade_ipv4,nf_conntrack_netlink,nf_conntrack_ipv4
   libcrc32c              12644  4 xfs,ip_vs,nf_nat,nf_conntrack
   
   拷贝到下面命令时，拷贝到EOF前即可，手动输入EOF，然后回车。
   配置开机自动导入ipvs模块
   [vagrant@lab1 ~]$ sudo tee /etc/modules-load.d/k8s-ipvs.conf <<-'EOF'
    ip_vs
    ip_vs_rr
    ip_vs_wrr
    ip_vs_sh
    nf_conntrack_ipv4
    EOF
   ip_vs
   ip_vs_rr
   ip_vs_wrr
   ip_vs_sh
   nf_conntrack_ipv4
   [vagrant@lab1 ~]$ 
   
   
   查看ipvs相关内核配置模块
   [vagrant@lab1 ~]$ sudo cat /etc/modules-load.d/k8s-ipvs.conf
   ip_vs
   ip_vs_rr
   ip_vs_wrr
   ip_vs_sh
   nf_conntrack_ipv4
   [vagrant@lab1 ~]$ 
   
   
   centos7 需要的特殊配置
   [vagrant@lab1 ~]$ sudo tee /etc/sysctl.d/k8s.conf <<-'EOF'
    net.bridge.bridge-nf-call-ip6tables = 1
    net.bridge.bridge-nf-call-iptables =1
    vm.swappiness=0
    EOF
   
   net.bridge.bridge-nf-call-ip6tables = 1
   net.bridge.bridge-nf-call-iptables =1
   vm.swappiness=0
   
   使配置生效
   [vagrant@lab1 ~]$ sudo sysctl --system
   * Applying /usr/lib/sysctl.d/00-system.conf ...
   net.bridge.bridge-nf-call-ip6tables = 0
   net.bridge.bridge-nf-call-iptables = 0
   net.bridge.bridge-nf-call-arptables = 0
   * Applying /usr/lib/sysctl.d/10-default-yama-scope.conf ...
   kernel.yama.ptrace_scope = 0
   * Applying /usr/lib/sysctl.d/50-default.conf ...
   kernel.sysrq = 16
   kernel.core_uses_pid = 1
   net.ipv4.conf.default.rp_filter = 1
   net.ipv4.conf.all.rp_filter = 1
   net.ipv4.conf.default.accept_source_route = 0
   net.ipv4.conf.all.accept_source_route = 0
   net.ipv4.conf.default.promote_secondaries = 1
   net.ipv4.conf.all.promote_secondaries = 1
   fs.protected_hardlinks = 1
   fs.protected_symlinks = 1
   * Applying /etc/sysctl.d/99-sysctl.conf ...
   * Applying /etc/sysctl.d/k8s.conf ...
   net.bridge.bridge-nf-call-ip6tables = 1
   net.bridge.bridge-nf-call-iptables = 1
   vm.swappiness = 0
   * Applying /etc/sysctl.conf ...
   [vagrant@lab1 ~]$ 
   
   
   
   开启forward
   [vagrant@lab1 ~]$ sudo iptables -P FORWARD ACCEPT
   [vagrant@lab1 ~]$ sudo sed -i '/Exec Start/a Exec Start Post=/sbin/iptables -P FORWARD ACCEPT' /usr/lib/systemd/system/docker.service
   [vagrant@lab1 ~]$ sudo systemctl daemon-reload
   [vagrant@lab1 ~]$ 
   
   
   配置host解析
   [vagrant@lab1 ~]$ sudo tee /etc/hosts <<-'EOF'
    127.0.0.1 localhost localhost.localdomain localhost4 localhost4.localdomain4
    ::1 localhost localhost.localdomain localhost6 localhost6.localdomain6
    11.11.11.111 lab1
    11.11.11.112 lab2
    11.11.11.113 lab3
    EOF
    
    
   127.0.0.1 localhost localhost.localdomain localhost4 localhost4.localdomain4
   ::1 localhost localhost.localdomain localhost6 localhost6.localdomain6
   11.11.11.111 lab1
   11.11.11.112 lab2
   11.11.11.113 lab3
   [vagrant@lab1 ~]$ 
   
   
   测试host ， -c2  意思是：ping2次。
   [vagrant@lab1 ~]$ ping -c2 lab1
   PING lab1 (11.11.11.111) 56(84) bytes of data.
   64 bytes from lab1 (11.11.11.111): icmp_seq=1 ttl=64 time=0.033 ms
   64 bytes from lab1 (11.11.11.111): icmp_seq=2 ttl=64 time=0.037 ms
   
   --- lab1 ping statistics ---
   2 packets transmitted, 2 received, 0% packet loss, time 999ms
   rtt min/avg/max/mdev = 0.033/0.035/0.037/0.002 ms
   [vagrant@lab1 ~]$ 
   
   ping -c2 lab2
   ping -c3 lab3
   
   
   
   
   配置kubelet
   [vagrant@lab1 ~]$ DOCKER_CGROUPS=$(sudo docker info | grep 'Cgroup' | cut -d' ' -f3)
   
   [vagrant@lab1 ~]$ echo $DOCKER_CGROUPS
   cgroupfs
   [vagrant@lab1 ~]$ sudo tee /etc/sysconfig/kubelet <<-EOF
    KUBELET_EXTRA_ARGS="--cgroup-driver=$DOCKER_CGROUPS--pod-infra-container-image=registry.cn-hangzhou.aliyuncs.com/google_containers/pause-amd64:3.1"
    EOF
    
    
   KUBELET_EXTRA_ARGS="--cgroup-driver=cgroupfs--pod-infra-container-image=registry.cn-hangzhou.aliyuncs.com/google_containers/pause-amd64:3.1"
   [vagrant@lab1 ~]$ 
   
   
   
   查看配置：
   [vagrant@lab1 ~]$ sudo cat /etc/sysconfig/kubelet
   KUBELET_EXTRA_ARGS="--cgroup-driver=cgroupfs--pod-infra-container-image=registry.cn-hangzhou.aliyuncs.com/google_containers/pause-amd64:3.1"
   [vagrant@lab1 ~]$ 
   
   
   重新加载配置：
   [vagrant@lab1 ~]$ sudo systemctl daemon-reload
   
   
   
   
   
   
   ```

 创建K8S集群

集群描述：

lab1：master 11.11.11.111

lab2：slave 11.11.11.112

lab3：slave 11.11.11.113



每台机器2个网卡。eth0，连接外网，eth1，内部通信。



 配置lab1



1. 配置docker  和  kubelet开机启动

   ```sh
   [vagrant@lab1 ~]$ sudo systemctl enable docker.service
   Created symlink from /etc/systemd/system/multi-user.target.wants/docker.service to /usr/lib/systemd/system/docker.service.
   [vagrant@lab1 ~]$ sudo systemctl enable kubelet.service
   Created symlink from /etc/systemd/system/multi-user.target.wants/kubelet.service to /usr/lib/systemd/system/kubelet.service.
   [vagrant@lab1 ~]$ 
   
   ```

2. 生成配置文件

   ```sh
   下面一大段都是命令
   
   cat >kubeadm-master.config<<EOF
   apiVersion: kubeadm.k8s.io/v1alpha2
   kind: MasterConfiguration
   kubernetesVersion: v1.12.4
   imageRepository: registry.cn-hangzhou.aliyuncs.com/google_containers
   api:
    advertiseAddress: 11.11.11.111
   
   controllerManagerExtraArgs:
    node-monitor-grace-period: 10s
    pod-eviction-timeout: 10s
   
   networking:
    podSubnet: 10.244.0.0/16
    
   kubeProxy:
    config:
    # mode: ipvs
    mode: iptables
    EOF
   ```

3. 提前拉取镜像

   ```sh
   [vagrant@lab1 ~]$ sudo kubeadm config images pull --config kubeadm-master.config
   [config/images] Pulled registry.cn-hangzhou.aliyuncs.com/google_containers/kube-apiserver:v1.12.4
   [config/images] Pulled registry.cn-hangzhou.aliyuncs.com/google_containers/kube-controller-manager:v1.12.4
   [config/images] Pulled registry.cn-hangzhou.aliyuncs.com/google_containers/kube-scheduler:v1.12.4
   [config/images] Pulled registry.cn-hangzhou.aliyuncs.com/google_containers/kube-proxy:v1.12.4
   [config/images] Pulled registry.cn-hangzhou.aliyuncs.com/google_containers/pause:3.1
   [config/images] Pulled registry.cn-hangzhou.aliyuncs.com/google_containers/etcd:3.2.24
   [config/images] Pulled registry.cn-hangzhou.aliyuncs.com/google_containers/coredns:1.2.2
   [vagrant@lab1 ~]$ 
   
   
   ```

   





