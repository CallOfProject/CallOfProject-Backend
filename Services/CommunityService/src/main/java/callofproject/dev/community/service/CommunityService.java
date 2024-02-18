package callofproject.dev.community.service;

import callofproject.dev.community.dto.CommunityDTO;
import callofproject.dev.community.mapper.ICommunityMapper;
import callofproject.dev.community.mapper.IUserMapper;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.data.common.status.Status;
import callofproject.dev.data.community.dal.CommunityServiceHelper;
import callofproject.dev.data.community.entity.Community;
import callofproject.dev.data.community.entity.User;
import callofproject.dev.library.exception.service.DataServiceException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;


@Component
@Lazy
public class CommunityService
{
    private final CommunityServiceHelper m_communityServiceHelper;
    private final ICommunityMapper m_communityMapper;
    private final IUserMapper m_userMapper;


    public CommunityService(CommunityServiceHelper communityServiceHelper, ICommunityMapper communityMapper, IUserMapper userMapper)
    {
        m_communityServiceHelper = communityServiceHelper;
        m_communityMapper = communityMapper;
        m_userMapper = userMapper;
    }

    /* public ResponseMessage<Object> findCommunityByProjectId(UUID projectId)
     {
         return doForDataService(() -> prepareCommunityResponseMessage(findCommunityIfExists(CommunityFindType.BY_PROJECT_ID, projectId)), "CommunityService::findCommunityByProjectId");
     }

     public ResponseMessage<Object> findCommunityByProjectOwnerId(UUID ownerId)
     {
         return doForDataService(() -> prepareCommunityResponseMessage(findCommunityIfExists(CommunityFindType.BY_PROJECT_OWNER_ID, ownerId)), "CommunityService::findCommunityByProjectOwnerId");
     }
 */
    public ResponseMessage<Object> findCommunityById(UUID communityId)
    {
        return doForDataService(() -> prepareCommunityResponseMessage(findCommunityIfExists(CommunityFindType.BY_ID, communityId)), "CommunityService::findCommunityById");
    }

    // ------------------ Private Methods ------------------ //
    private ResponseMessage<Object> prepareCommunityResponseMessage(Optional<Community> community)
    {
        var msg = community.isEmpty() ? "Community not found" : "Community is found!";
        var status = community.isEmpty() ? Status.NOT_FOUND : Status.OK;
        var communityDTO = community.map(this::toCommunityDTO).orElse(null);

        return new ResponseMessage<>(msg, status, communityDTO);
    }

    private CommunityDTO toCommunityDTO(Community community)
    {
        var projectOwner = findUserIfExist(community.getProject().getProjectOwner().getUserId());

        var projectOwnerDTO = m_userMapper.toUserDTO(projectOwner);

        return m_communityMapper.toCommunityDTO(community, projectOwnerDTO);
    }

    private Optional<Community> findCommunityIfExists(CommunityFindType type, UUID id)
    {
        return switch (type)
        {
            //case BY_PROJECT_ID -> m_communityServiceHelper.findCommunityByProjectId(id);
            //case BY_PROJECT_OWNER_ID -> m_communityServiceHelper.findCommunityByProjectOwnerId(id);
            case BY_ID -> m_communityServiceHelper.findCommunityById(id);
            default -> throw new DataServiceException("Invalid community find type");
        };
    }

    private User findUserIfExist(UUID userId)
    {
        return m_communityServiceHelper.findUserById(userId).orElseThrow(() -> new DataServiceException("User not found"));
    }
}