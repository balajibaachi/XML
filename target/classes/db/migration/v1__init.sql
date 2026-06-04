CREATE TABLE jobs
(
    id UUID PRIMARY KEY,

    created_at TIMESTAMP,

    updated_at TIMESTAMP,

    status VARCHAR(50),

    total_urls INT,

    completed INT,

    failed INT
);

CREATE TABLE tasks
(
    id UUID PRIMARY KEY,

    created_at TIMESTAMP,

    updated_at TIMESTAMP,

    job_id UUID REFERENCES jobs(id),

    url TEXT,

    status VARCHAR(50),

    records_extracted INT,

    error TEXT
);

CREATE TABLE articles
(
    id UUID PRIMARY KEY,

    created_at TIMESTAMP,

    updated_at TIMESTAMP,

    task_id UUID REFERENCES tasks(id),

    title TEXT,

    link TEXT,

    author TEXT,

    published_date TIMESTAMP,

    summary TEXT
);