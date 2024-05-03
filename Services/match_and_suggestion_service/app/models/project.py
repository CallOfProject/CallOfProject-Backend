class Project:
    def __init__(self, project_id, project_name, project_tags):
        self.project_id = project_id
        self.project_name = project_name
        self.project_tags = project_tags

    def __repr__(self):
        return f"Project(project_id={self.project_id}, project_name={self.project_name}, project_tags={self.project_tags})"
