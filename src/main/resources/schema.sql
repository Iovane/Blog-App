create table app_user
(
    id         bigint generated by default as identity primary key,
    username   varchar(255) not null
        constraint uk3k4cplvh82srueuttfkwnylq0 unique,
    password   varchar(255) not null,
    email      varchar(255) not null
        constraint uk1j9d9a06i600gd43uu3km82jw unique,
    created_at timestamp(6),
    updated_at timestamp(6),
    updated_by varchar(255)
);

create table public.authority
(
    id      bigint generated by default as identity
        primary key,
    name    varchar(255),
    user_id bigint
        constraint fkcqwbvgnkqj13depvim3sp30e3
            references app_user
);

create table post
(
    id         bigint generated by default as identity primary key,
    content    varchar(255),
    title      varchar(255),
    author_id  bigint not null
        constraint fklhq00vlc2iwxk5tao1bhx8jbj references app_user,
    created_at timestamp(6),
    updated_at timestamp(6),
    updated_by varchar(255)
);

create table comment
(
    id         bigint generated by default as identity primary key,
    content    varchar(255),
    user_id    bigint not null
        constraint fk37mjvnvpwbqdpewm39q75h9q references app_user,
    post_id    bigint not null
        constraint fks1slvnkuemjsq2kj4h3vhx7i1 references post,
    created_at timestamp(6),
    updated_at timestamp(6),
    updated_by varchar(255)
);

ALTER SEQUENCE app_user_id_seq restart WITH 3;
ALTER SEQUENCE authority_id_seq restart WITH 4;