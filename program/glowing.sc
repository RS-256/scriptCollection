global_app_name = system_info('app_name');
global_scriptVersion = '1.0.0';
global_author = 'RS256';
global_defaultDirectory = global_app_name + '/';

global_help_page = [
	'/%appname%'
];

_error(error) -> exit(print(format(str('r %s', error))));

__config() -> {
	'commands' ->{
		'' -> 'credit',
		'give <players>' -> ['modifyEffect', 5, false, false],
		'give <players> <duration>' -> ['modifyEffect', false, false],
		'give <players> <duration> <show_particles>' -> ['modifyEffect', false],
		'give <players> <duration> <show_particles> <show_icon>' -> 'modifyEffect',
		'clear <players>' -> ['modifyEffect', 0, false, false]
	},
	'arguments' -> {
		'players' -> {
			'type' -> 'players'
		},
		'duration' -> {
			'type' -> 'int',
			'min' -> -1,
			'max' -> 1000000,
			'suggest' -> [
				'5',
				'30',
				'60'
			]
		},
		'show_particles' -> {
			'type' -> 'bool'
		},
		'show_icon' -> {
			'type' -> 'bool'
		}
	}
};

credit() -> (
	texts = 'version : ' + global_scriptVersion + '\nauthor : ' + global_author;
	print(texts);
);

modifyEffect(players, duration, show_particles, show_icon) -> (
	if(duration != -1, duration = duration * 20);
	for(players,
		if(player(_) != null,
			modify(player(_), 'effect', 'glowing', duration, 1, show_particles, show_icon);
			sound('minecraft:entity.experience_orb.pickup', player(_)~'pos', 1, 1, 'master');
			if(duration != 0,
				successText = str('%s is highlighted', _);,
				successText = str('%s is hidden', _);
			);
			logger('warn', successText);
			print(player('all'), successText);,
			errorText = format('w mcid: ', str('b %s ', _), 'w is offline player or not player');
			_error(errorText);
		);
	);
);