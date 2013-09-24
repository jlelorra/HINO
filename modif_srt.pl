#!/usr/bin/perl -w

use strict;
use warnings;
use Getopt::Long;

my $FILE;
my $DELTA;
my $DELTAMILLI;
############################GESTION DES OPTIONS##################################"

my ($opt_h, $opt_ver, $opt_file , $opt_seconds, $opt_milliseconds);
GetOptions(
           "h"   => \$opt_h, 		"help"  	=> \$opt_h,
           "v"   => \$opt_ver,    	"version"  	=> \$opt_ver,
           "f=s"   => \$opt_file, 	"file=s"  	=> \$opt_file,
           "s=s"   => \$opt_seconds,	"second=s" 	=> \$opt_seconds,
           "ms=s"   => \$opt_milliseconds,	"millisecond=s" => \$opt_milliseconds,
);


if ($opt_h) {
    print_help();
    exit;
}

if ($opt_ver) {
    print_version();
    exit;
}

if ($opt_file) {
        $FILE= $opt_file;
}

if ($opt_seconds) {
        $DELTA =  $opt_seconds;
}

if ($opt_milliseconds) {
        $DELTAMILLI =  $opt_milliseconds;
}


sub print_help {
    print "    Usage: $0 {-v -h } -f \"FILENAME\" -s \"DELTATIME\" -ms \"DELTATIME\" \n";
    print "    2013 Jason LELORRAIN
    This program is free software; you can redistribute it and/or modify it under the same terms as Perl itself. 

    -h, --help
        Display detailed help
    -v, --version
        Show version information
    -f, --file
        filename to modify
    -s, --second
        time to add of srt balise in seconds
    -ms, --millisecond
        time to add of srt balise in millisecond
    
    This program will create a new srt file which will be in time with the delta include by parameter -s or --second and -ms or --millisecond
      

";

exit 0
}

sub print_version {
        print "
    $0 version 1.02 - September 24th, 2013
    2013 Jason LELORRAIN 
    This program is free software; you can redistribute it and/or modify it under the same terms as Perl itself. 
";
exit 0
}

#####################################################################################################################################################
## remplace dans le fichier srt courant toutes les balises de temps en ajoutant un delta (passé en paramétre -s ou -ms) en seconds ou milliseconds.##
#####################################################################################################################################################

open(FILE,"+<$FILE") or die"open: $!";
my($line,$word);
$word=0;
$opt_file=~s/.srt/.2.srt/;
my $newfilename = $opt_file;
while( defined( $line = <FILE> ) ) {
	open(FIC,">>$newfilename");
	if ($line =~/[0-9]{2}\:[0-9]{2}\:[0-9]{2}\,[0-9]{3}\ \-\-\>\ [0-9]{2}\:[0-9]{2}\:[0-9]{2}\,[0-9]{3}/){
		if($DELTA && $DELTAMILLI){
			my $badtime1= substr($line, 6, 2);
			my $badtime2= substr($line, 23, 2);
			my $badtime3= substr($line, 9, 3);
			my $badtime4= substr($line, 26, 3);
			if($badtime1=~/[0-9]{2}/ && $badtime2=~/[0-9]{2}/ && $badtime3=~/[0-9]{3}/ && $badtime4=~/[0-9]{3}/){
				my $goodtime1= $badtime1 + $DELTA;
				my $goodtime2= $badtime2 + $DELTA;
				my $goodtime3= $badtime3 + $DELTAMILLI;
				my $goodtime4= $badtime4 + $DELTAMILLI;
				if($goodtime3>=1000){$goodtime3=999;}
				if($goodtime4>=1000){$goodtime4=999;}
				$line=substr($line, 0, 6).${goodtime1}.",".${goodtime3}.substr($line, 12, 11).${goodtime2}.",".${goodtime4}."\n";
				print (FIC "$line");
				$word++;
			}
		}else{
			if($DELTA){
				my $badtime1= substr($line, 6, 2);
				my $badtime2= substr($line, 23, 2);
				if($badtime1=~/[0-9]{2}/ && $badtime2=~/[0-9]{2}/){
					my $goodtime1= $badtime1 + $DELTA;
					my $goodtime2= $badtime2 + $DELTA;
					$line=substr($line, 0, 6).${goodtime1}.substr($line, 8, 15).${goodtime2}.substr($line, 25, 4)."\n";
					print (FIC "$line");
					$word++;
				}
			}
			if($DELTAMILLI){
				my $badtime3= substr($line, 9, 3);
				my $badtime4= substr($line, 26, 3);
				if($badtime3=~/[0-9]{3}/ && $badtime4=~/[0-9]{3}/){
					my $goodtime3= $badtime3 + $DELTAMILLI;
					my $goodtime4= $badtime4 + $DELTAMILLI;
					if($goodtime3>=1000){$goodtime3=999;}
					if($goodtime4>=1000){$goodtime4=999;}
					$line=substr($line, 0, 9).${goodtime3}.substr($line, 12, 14).${goodtime4}."\n";
					print (FIC "$line");
					$word++;
				}
			}
		}
	}else{
		print (FIC "$line");
	}
	close(FIC);
}
close(FILE);
print "$FILE a ete modifier $word fois.\n";
exit 0;
