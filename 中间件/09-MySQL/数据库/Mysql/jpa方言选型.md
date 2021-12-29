### 优化方案

1、关闭flyway
2、打开jpa自动建表功能
3、替换jpa的hibernate组件方言：org.hibernate.dialect.MySQL57InnoDBDialect

### 各环境存量表优化

dev环境

ALTER TABLE account_sys_entity ENGINE=InnoDB;
ALTER TABLE account_sys_entity_restore ENGINE=InnoDB;
ALTER TABLE app_relate_developer_account ENGINE=InnoDB;
ALTER TABLE app_relate_third_account ENGINE=InnoDB;
ALTER TABLE child_authority_entity_restore ENGINE=InnoDB;
ALTER TABLE hibernate_sequence ENGINE=InnoDB;
ALTER TABLE hibernate_sequence_deleted ENGINE=InnoDB;
ALTER TABLE my_user_relate_question_restore ENGINE=InnoDB;
ALTER TABLE wechat_user_entity ENGINE=InnoDB;

uat环境

ALTER TABLE account_sys_entity ENGINE=InnoDB;
ALTER TABLE account_sys_entity_new ENGINE=InnoDB;
ALTER TABLE account_sys_entity_restore ENGINE=InnoDB;
ALTER TABLE app_relate_developer_account ENGINE=InnoDB;
ALTER TABLE child_authority_entity_restore ENGINE=InnoDB;
ALTER TABLE hibernate_sequence  ROW_FORMAT=DYNAMIC;
ALTER TABLE hibernate_sequence ENGINE=InnoDB;
ALTER TABLE my_user_relate_question_restore ENGINE=InnoDB;
ALTER TABLE user_assign_authorities_deleted ENGINE=InnoDB;
ALTER TABLE user_assign_authority_groups_deleted ENGINE=InnoDB;
ALTER TABLE wechat_user_entity ENGINE=InnoDB;

pro环境

使用RDB，默认建表是用InnoDB引擎

