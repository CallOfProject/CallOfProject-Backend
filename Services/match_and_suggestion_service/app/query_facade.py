def get_all_users():
    return """
            SELECT
        u.user_id,
        u.username,
        GROUP_CONCAT(DISTINCT e.school_name) AS educations_arr,
        GROUP_CONCAT(DISTINCT ut.tag_name) AS user_tags,
        GROUP_CONCAT(DISTINCT ex.job_definition) AS experiences
    FROM
        cop_user AS u
    JOIN
        user_profile AS up ON u.user_profile_id = up.user_profile_id
    LEFT JOIN
        user_profiles_to_education AS upe ON up.user_profile_id = upe.user_profile_id
    LEFT JOIN
        education AS e ON upe.education_id = e.education_id
    LEFT JOIN
        user_profiles_to_tags AS upt ON up.user_profile_id = upt.user_profile_id
    LEFT JOIN
        user_tag AS ut on upt.tag_id = ut.tag_id
    LEFT JOIN
        user_profiles_to_experience AS upex ON up.user_profile_id = upex.user_profile_id
    LEFT JOIN
        experience AS ex on upex.experience_id = ex.experience_id
    GROUP BY
        u.user_id,
        u.username
    """


def search_user_by_id(user_id):
    return f"""
            SELECT
        u.user_id,
        u.username,
        GROUP_CONCAT(DISTINCT e.school_name) AS educations_arr,
        GROUP_CONCAT(DISTINCT ut.tag_name) AS user_tags,
        GROUP_CONCAT(DISTINCT ex.job_definition) AS experiences
    FROM
        cop_user AS u
    JOIN
        user_profile AS up ON u.user_profile_id = up.user_profile_id
    LEFT JOIN
        user_profiles_to_education AS upe ON up.user_profile_id = upe.user_profile_id
    LEFT JOIN
        education AS e ON upe.education_id = e.education_id
    LEFT JOIN
        user_profiles_to_tags AS upt ON up.user_profile_id = upt.user_profile_id
    LEFT JOIN
        user_tag AS ut ON upt.tag_id = ut.tag_id
    LEFT JOIN
        user_profiles_to_experience AS upex ON up.user_profile_id = upex.user_profile_id
    LEFT JOIN
        experience AS ex ON upex.experience_id = ex.experience_id
    WHERE
        u.user_id = uuid_to_bin('{user_id}')
    GROUP BY
        u.user_id,
        u.username
    """


def get_all_projects():
    return "select project_id, title from project"
