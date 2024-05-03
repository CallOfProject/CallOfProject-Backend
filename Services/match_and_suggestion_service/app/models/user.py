class User:
    def __init__(self, user_id, username, universities, user_tags, experiences):
        self.user_id = user_id
        self.username = username
        self.universities = universities
        self.user_tags = user_tags
        self.experiences = experiences

    def __repr__(self):
        return f"User(user_id={self.user_id}, username={self.username}, universities={self.universities}, user_tags={self.user_tags}, experiences={self.experiences})"
