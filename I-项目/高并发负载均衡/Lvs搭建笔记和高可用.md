LVS：

node01:
	ifconfig  eth0:8 192.168.150.100/24
node02~node03:
	1)修改内核：
		echo 1  >  /proc/sys/net/ipv4/conf/eth0/arp_ignore 
		echo 1  >  /proc/sys/net/ipv4/conf/all/arp_ignore 
		echo 2 > /proc/sys/net/ipv4/conf/eth0/arp_announce 
		echo 2 > /proc/sys/net/ipv4/conf/all/arp_announce 
	2）设置隐藏的vip：
		ifconfig  lo:3  192.168.150.100  netmask 255.255.255.255
		
RS中的服务：
node02~node03:
	yum install httpd -y
	service httpd start
	vi   /var/www/html/index.html
		from 192.168.150.1x

LVS服务配置
node01:
		yum install ipvsadm 
	ipvsadm -A  -t  192.168.150.100:80  -s rr
	ipvsadm -a  -t 192.168.150.100:80  -r  192.168.150.12 -g -w 1
	ipvsadm -a  -t 192.168.150.100:80  -r  192.168.150.13 -g -w 1
	ipvsadm -ln

验证：
	浏览器访问  192.168.150.100   看到负载  疯狂F5
	node01：
		netstat -natp   结论看不到socket连接
	node02~node03:
		netstat -natp   结论看到很多的socket连接
	node01:
		ipvsadm -lnc    查看偷窥记录本
		TCP 00:57  FIN_WAIT    192.168.150.1:51587 192.168.150.100:80 192.168.150.12:80
		FIN_WAIT： 连接过，偷窥了所有的包
		SYN_RECV： 基本上lvs都记录了，证明lvs没事，一定是后边网络层出问题
	
	
	
	



keepalived实验：
主机： node01~node04

node01:
	ipvsadm -C  清楚之前的负载策略配置
	ifconfig eth0:8 down  把上一步手动配的虚拟网卡停掉。这些后面都通过keepalived实现。

以上步骤是为了吧node01还原成裸机。

---

keepalived配置：


```yml
node01,node04:
	yum install keepalived ipvsadm -y
	配置：
		cd  /etc/keepalived/
		cp keepalived.conf keepalived.conf.bak
		vi keepalived.conf
			node01:
			vrrp：虚拟路由冗余协议！
				vrrp_instance VI_1 {
					state MASTER         //  node04  BACKUP
					interface eth0
					virtual_router_id 51
					priority 100		 //	 node04	 50
					advert_int 1
					authentication {
						auth_type PASS
						auth_pass 1111
					}
					virtual_ipaddress {
						192.168.150.100/24 dev eth0 label  eth0:3
					}
				}
			virtual_server 192.168.150.100 80 {
				delay_loop 6
				lb_algo rr
				lb_kind DR
				nat_mask 255.255.255.0
				persistence_timeout 0  #生产环境的时候要改一个合适的时间，实验环境暂时不缓存
				protocol TCP

				real_server 192.168.150.12 80 {
					weight 1
					HTTP_GET {
						url {
						  path /
						  status_code 200
						}
						connect_timeout 3
						nb_get_retry 3
						delay_before_retry 3
					}   
				}       
				real_server 192.168.150.13 80 {
					weight 1
					HTTP_GET {
						url {
						  path /
						  status_code 200
						}
						connect_timeout 3
						nb_get_retry 3
						delay_before_retry 3
					}
				}
				
			#scp 远程复制	
			scp  ./keepalived.conf  root@node04:`pwd`

```

