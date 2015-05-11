package com.daynamus.web;

import java.util.ArrayList;
import java.util.List;

import org.exoplatform.commons.utils.ListAccess;
import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.listener.Event;
import org.exoplatform.services.listener.Listener;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.security.ConversationRegistry;
import org.exoplatform.services.security.ConversationState;
import org.exoplatform.social.core.space.SpaceException;
import org.exoplatform.social.core.space.model.Space;
import org.exoplatform.social.core.space.spi.SpaceService;

import com.daynamus.service.UserService;

public class CustomLoginListener extends
		Listener<ConversationRegistry, ConversationState> {

	private static final Log LOG = ExoLogger
			.getLogger(CustomLoginListener.class);

	/**
	 * {@inheritDoc}
	 */
	public void onEvent(Event<ConversationRegistry, ConversationState> event)
			throws Exception {
		String userId = event.getData().getIdentity().getUserId();

		LOG.info("Apply listeners for user " + userId);
		UserService.connectedusermap.put(userId, "login");

		LOG.info("User listeners applied for " + userId);

	}

	public static List<String> showMySpaceURLs(String userId) {
		List<String> mySpacesURLs = new ArrayList<String>();
		SpaceService _spaceService = (SpaceService) PortalContainer
				.getInstance().getComponentInstanceOfType(SpaceService.class);
		StringBuffer baseSpaceURL = null;

		try {
			ListAccess<Space> la = _spaceService
					.getAccessibleSpacesWithListAccess(userId);
			Space[] spaces = la.load(0, la.getSize());

			for (Space space : spaces) {
				baseSpaceURL = new StringBuffer();
				 baseSpaceURL.append(PortalContainer.getCurrentPortalContainerName()+ "/g/:spaces:") ;
				String groupId = space.getGroupId();
				String permanentSpaceName = groupId.split("/")[2];
				if (permanentSpaceName.equals(space.getPrettyName())) {
					baseSpaceURL.append(permanentSpaceName);
					baseSpaceURL.append("/");
					baseSpaceURL.append(permanentSpaceName);
				} else {
					baseSpaceURL.append(space.getPrettyName());
					baseSpaceURL.append("/");
					baseSpaceURL.append(space.getPrettyName());
				}
				mySpacesURLs.add(baseSpaceURL.toString());

			}

		} catch (SpaceException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mySpacesURLs;

	}

}
