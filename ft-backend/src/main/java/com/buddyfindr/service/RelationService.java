package com.buddyfindr.service;

import com.buddyfindr.dto.RelationDto;
import com.buddyfindr.dto.PetSimpleInfoDto;
import java.util.List;

public interface RelationService {
    void follow(RelationDto dto);
    void unfollow(RelationDto dto);
    void block(RelationDto dto);
    void unblock(RelationDto dto);
    List<PetSimpleInfoDto> getFollowing(Long petId);
    List<PetSimpleInfoDto> getFollowers(Long petId);
    List<PetSimpleInfoDto> getBlocked(Long petId);
} 