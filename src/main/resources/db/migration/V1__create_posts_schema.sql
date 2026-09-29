CREATE TABLE posts (
    id UUID PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    category VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE post_tags (
    post_id UUID NOT NULL,
    tag VARCHAR(50) NOT NULL,
    CONSTRAINT fk_post_tags_post
        FOREIGN KEY (post_id) REFERENCES posts (id) ON DELETE CASCADE
);

CREATE INDEX idx_posts_created_at ON posts (created_at DESC);
CREATE INDEX idx_posts_category ON posts (category);
CREATE INDEX idx_post_tags_post_id ON post_tags (post_id);
