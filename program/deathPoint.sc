global_app_name = system_info('app_name');
global_scriptVersion = '1.0.0';
global_author = 'RS256';
global_defaultDirectory = global_app_name + '/';

global_xaero_waypoint = 'waypoint:name:initials:x:y:z:color:disabled:type:set:rotate_on_tp:tp_yaw:visibility_type:destination';
global_waypoint_template = 'xaero-waypoint:death point:DP:%d:%d:%d:12:false:0:Internal-%s-waypoints';

global_share_text = true;
global_share_waypoint = true;

global_share_creative = false;

__config() -> {
	'commands' -> {
		'' -> 'credit',
		'text' -> ['changeShareText', global_share_text],
		'text true' -> ['changeShareText', true],
		'text false' -> ['changeShareText', false],
		'waypoint' -> ['changeShareWaypoint', global_share_waypoint],
		'waypoint true' -> ['changeShareWaypoint', true],
		'waypoint false' -> ['changeShareWaypoint', false]
	}
};

credit() -> (
	texts = 'version : ' + global_scriptVersion + '\nauthor : ' + global_author;
	print(texts);
);

changeShareText(value) -> (
	global_share_text = value;
	stat = null;
	if(global_share_text == false, stat = 'r disabled');
	if(global_share_text == true, stat = 'l enabled');
	print(player('all'), format(
		str('w text sharing is now '),
		str(stat)
	));
);

changeShareWaypoint(value) -> (
	global_share_waypoint = value;
	stat = null;
	if(global_share_waypoint == false, stat = 'r disabled');
	if(global_share_waypoint == true, stat = 'l enabled');
	print(player('all'), format(
		str('w waypoint sharing is now '),
		str(stat)
	));
);

__on_player_dies(player) -> (
	playerX = player(player)~'x';
	playerY = player(player)~'y';
	playerZ = player(player)~'z';
	playerDimension = player(player)~'dimension';
	
	if(global_share_text == true,
		text = format(
			str('w %s ', player(player)~'name'),
			str('r is just died! '),
			str('w at '),
			str('c (%d, %d, %d) ', playerX, playerY, playerZ),
			str('w in '),
			str('l %s', playerDimension)
		);
		print(player('all'), text);
	);
	
	if (global_share_waypoint == true,
		waypoint =str(global_waypoint_template, playerX, playerY, playerZ, playerDimension);
		print(player('all'), waypoint);
	);
);