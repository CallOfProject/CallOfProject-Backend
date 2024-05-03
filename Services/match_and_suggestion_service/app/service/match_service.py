async def recommend_projects_for_user(user, projects):
    recommended_projects = []

    for project in projects:
        common_tags = set(user.user_tags).intersection(set(project.project_tags))

        # {"project_name": project.project_name,"common_tags": list(common_tags),}
        if common_tags:
            recommended_projects.append(project.project_id)

    return recommended_projects


async def recommend_users_by_tags(target_user, all_users):
    recommended_users = []

    for user in all_users:
        if user.user_id == target_user.user_id:
            continue

        common_tags = set(target_user.user_tags).intersection(user.user_tags)

        #  {"username": user.username,"common_tags": list(common_tags)}
        if common_tags:
            recommended_users.append(user.user_id)

    return recommended_users


async def recommend_users_by_universities(target_user, all_users):
    recommended_users = []

    for user in all_users:
        if user.user_id == target_user.user_id:
            continue

        common_universities = set(target_user.universities).intersection(user.universities)

        # {"username": user.username,"common_universities": list(common_universities)}
        if common_universities:
            recommended_users.append(user.user_id)

    return recommended_users


async def recommend_users_by_experiences(target_user, all_users):
    recommended_users = []

    for user in all_users:
        if user.user_id == target_user.user_id:
            continue

        common_experiences = set(target_user.experiences).intersection(user.experiences)

        # {"username": user.username,"common_experiences": list(common_experiences)}
        if common_experiences:
            recommended_users.append(user.user_id)

    return recommended_users


async def recommend_users(target_user, all_users):
    recommended_users = []

    for user in all_users:

        if user.user_id == target_user.user_id:
            continue

        common_universities = set(target_user.universities).intersection(user.universities)

        common_experiences = set(target_user.experiences).intersection(user.experiences)

        common_tags = set(target_user.user_tags).intersection(user.user_tags)

        if common_universities or common_experiences or common_tags:
            recommendation_details = {
                "username": user.username,
                "common_universities": list(common_universities),
                "common_experiences": list(common_experiences),
                "common_tags": list(common_tags),
            }
            print(recommendation_details)
            recommended_users.append(user.user_id)

    return recommended_users
