#!/usr/bin/perl

#===============================================================================
# Auteur : JLE
# Date   : 12/06/2013 
# But    : Plugin retournant l'etat des interfaces des serveurs "GOD"
#===============================================================================
use strict;
use warnings;
use Getopt::Long;
use Term::ANSIColor;
#use DateTime;
#use DateTime::TimeZone;

#arborescence des fichiers
my $PathrepoSnmp="/etc/snmp/";
my $PathrepoSmtp="/etc/postfix/";
my $Pathrepoldap="/etc/ldap/";
my $Pathnrpe="/etc/nagios/";


#nom des fichiers
my $ldap="ldap.conf";
my $ntp="/etc/ntp.conf";
my $dns="/etc/resolv.conf";
my $snmp="snmpd.conf";
my $smtp="main.cf";
my $nrpe="nrpe.cfg";

#VARIABLE GLOBAL
my $OS="RHEL";
my $datestamp;
my $timestamp;
my $package;
my $IP_LDAP;
my $IP_NTP;
my $IP_DNS;
my $IP_SNMP;
my $IP_SMTP_RH;
my $IP_SMTP_FB;
my $IP_NRPE;
my $GLOBAL_CONF;
my $GLOBAL_VAR;

my ($opt_conf, $opt_dns, $opt_change_path, $opt_h, $opt_ldap,$opt_smtp, $opt_ntp,$opt_nrpe, $opt_snmp, $opt_ver, $opt_result,$opt_insert);
GetOptions(
			"c"  	=> \$opt_conf, 			"conf_puppet"  	=> \$opt_conf,
			"d=s"  => \$opt_dns, 			"dns=s"  			=> \$opt_dns,
			"e"   	=> \$opt_change_path, 	"change_path"  	=> \$opt_change_path,
           "h"   	=> \$opt_h, 			"help"  			=> \$opt_h,
           "l=s" 	=> \$opt_ldap, 			"ldap=s"  			=> \$opt_ldap,
           "m=s" 	=> \$opt_smtp, 			"smtp=s"  			=> \$opt_smtp,
			"n=s" 	=> \$opt_ntp, 			"ntp=s"  			=> \$opt_ntp,
			"p=s" 	=> \$opt_nrpe, 			"nrpe=s"  			=> \$opt_nrpe,
           "r"   	=> \$opt_result,    	"result"  			=> \$opt_result,
           "s=s" 	=> \$opt_snmp,   		"snmp=s"  			=> \$opt_snmp,           
           "v"   	=> \$opt_ver,    		"version"  		=> \$opt_ver,
           "i"		=> \$opt_insert,		"insert"			=> \$opt_insert,
);

#if ($opt_insert) {
#    &insert_conf_file("DNS");
#}

if($opt_change_path){
	&insert_path;
}


if ($opt_conf) {
	&parametre;
    &delta_conf_puppet;

}

if ($opt_h) {
    print_help();
    exit;
}

if ($opt_ver) {
        print_version();
        exit;
}

if ($opt_result) {
        print_result();
}


if($opt_dns || $opt_ldap || $opt_smtp || $opt_ntp || $opt_nrpe || $opt_snmp){
	 &parametre;
}

if ($opt_dns) {
	foreach my $opt (split(',',$opt_dns)) {
			&get_conf_dns;	
	       	&test_conf($IP_DNS,"CONF DNS",$opt);
	}
}

if ($opt_ldap) {
	foreach my $opt (split(',',$opt_ldap)) {
		&get_conf_ldap;
       	&test_conf($IP_LDAP,"CONF LDAP",$opt);
	}
}

if ($opt_smtp) {
	foreach my $opt (split(',',$opt_smtp)) {
		 &get_conf_smtp;
		 if($IP_SMTP_RH eq $IP_SMTP_FB){
	     	&test_conf($IP_SMTP_RH,"CONF SMTP",$opt);
		 }else{
			if($IP_SMTP_RH){
				$IP_SMTP_RH=~s/ //g;
				$IP_SMTP_FB=~s/ //g;
				$opt=~s/ //g;
				if($IP_SMTP_FB ne $opt){
			    	&test_conf($IP_SMTP_RH,"CONF SMTP relayhost",$opt);
			    	$IP_SMTP_RH=~s/ //g;
				}
		    	if($IP_SMTP_RH ne $opt){
		        	&test_conf($IP_SMTP_FB,"CONF SMTP fallback-relay",$opt);
		    	}
		 	}
	    }
	}
}

if ($opt_ntp) {
	foreach my $opt (split(',',$opt_ntp)) {
		 &get_conf_ntp;	
       	 &test_conf($IP_NTP,"CONF NTP",$opt);
	}
}

if ($opt_nrpe) {
	foreach my $opt (split(',',$opt_nrpe)) {
		 &get_conf_nrpe;	
       	&test_conf($IP_NRPE,"CONF NRPE",$opt);
	}

}

if ($opt_snmp) {
	foreach my $opt (split(',',$opt_snmp)) {
		 &get_conf_snmp;	
       	&test_conf($IP_SNMP,"CONF SNMP",$opt_snmp);
	}
}


sub print_help {
	
print "	
usage: get_conf_distant {-c -e -h -r -v -d [\@DNS1,\@DNS2...] -l [\@LDAP1,\@LDAP2...] -m [\@SMTP1,\@SMTP2...] -n [\@NTP1,\@NTP2...] -p [\@NRPE1,\@NRPE2...] -s [\@SNMP1,\@SNMP2...]}
recupere, test et affiche et modifie au besoin les configurations des services presents sur l'equipements

 -c => delta_Conf_puppet
 -d => Dns
 -e => changE_puppet_server_path
 -h => Help
 -l => Ldap
 -m => sMtp
 -n => Ntp
 -p => nrPe
 -r => print Results (like version 1.01)
 -s => Snmp
 -v => Version


";
exit 0;

}

sub print_version {
        print "
        $0  version 1.01 - June 17th, 2013
    2013 Jason LELORRAIN 
    This program is free software; you can redistribute it and/or modify it under the same terms as Perl itself. 
";
}

sub echo_color_green($) {
	my ($txt)=@_;
	print "[";
	print color 'green';
	print"OK";
	print color 'reset';
	print "] $txt \n";	

}

sub echo_color_red($) {
	my ($txt)=@_;
	print "[";
	print color 'red';
	print"NOK";
	print color 'reset';
	print "] $txt \n";

}

sub echo_color_blue($) {
	my ($txt)=@_;
	print color 'blue';
	print "$txt \n";
	print color 'reset';
}

sub echo_color_yellow($) {
	my ($txt)=@_;
	print color 'yellow';
	print "$txt \n";
	print color 'reset';
}

sub CALDATE() {
# Commande recuperant l'heure et la date systeme
# ATTENTION VARIABLE DATE & TIME DIFF EN Fr et Us
my ($month,$day) = @_;
my ($sec,$min,$hour,$mday,$mon,$year,$wday,$yday,$isdst ,$usec) = localtime(time); ## Commande recuperant l'heure et la date systeme
$month = $mon + 1;
$month = sprintf ("%02d",$month);
$day = sprintf ("%02d",$mday);
$hour = sprintf ("%02d",$hour);
$min = sprintf ("%02d",$min);
$sec = sprintf ("%02d",$sec);
#$usec = `date +.%s"`;
my $time = "$hour.$min.$sec";
$year += 1900;
my $date = "$day.$month.$year";
$datestamp = "${day}${month}${year}";
$timestamp = "$hour.$min.$sec";
#my $dt = DateTime->new(year => $year, month => $m, day => $d, hour => $h, minute => $min, second => $s, time_zone => "UTC");
#my $real_dt = $dt->clone() ->set_time_zone( 'Europe/Paris' );
}


sub check_os {
	
	$OS =`cat /etc/issue | grep -iE 'Ubuntu|Debian'`;
	unless ($OS){
		$OS="debian";
	}
	
}

sub get_conf_ldap{
	
		#echo "----------------------------------conf_LDAP : " >> tmp.txt
	    if ( -f "${Pathrepoldap}$ldap"){
			if(-f "/etc/libnss-ldap.conf"){
				$package="/etc/libnss-ldap.conf";
			}else{ 
				$package="$Pathrepoldap$ldap";
		    }
		   $IP_LDAP=`grep 'host ' ${package} | sed "s+host ++g" | cut -d "#" -f1`;
		   $IP_LDAP=~ s/\n/ /g;
	    }
}

sub get_conf_ntp{
	
		#echo "----------------------------------conf_NTP : " >> ../$HOME/tmp.txt
	    if (-f $ntp){
		    $package=$ntp;
		    $IP_NTP=` grep 'server ' ${package} | sed "s+server ++g" | cut -d "#" -f1`;
		    $IP_NTP=~ s/\n/ /g;
		}
}

sub get_conf_dns{
	
		#echo "----------------------------------conf_DNS : " >> ../$HOME/tmp.txt
	    if (-f $dns){
	    	 $package=$dns;
		    $IP_DNS=`grep nameserver $package | grep -v '#' | sed "s+nameserver ++g" | sed "s+ ++g"`;
		    $IP_DNS=~ s/\n/ /g;
		}
}

sub get_conf_snmp{
	
		#echo "----------------------------------conf_SNMP : " >> ../$HOME/tmp.txt
	    if (-d $PathrepoSnmp){
		    $package="$PathrepoSnmp$snmp";
		    if($OS eq "debian"){
		    	$IP_SNMP=`grep 'com2sec ' $package | grep -v '#' | sed "s+com2sec ++g" | sed "s+public++g" | sed "s+readonly ++g" `;
		    }else{
		    	$IP_SNMP=`grep 'com2sec ' $package | grep -v '#' | sed "s+com2sec ++g" | sed "s+public++g" | sed "s+readonly ++g" `;
	    	}
	    	$IP_SNMP=~ s/\n/ /g;
	    }
}

sub get_conf_smtp{
	
		#echo "----------------------------------conf_SMTP : " >> ../$HOME/tmp.txt
	    if(-d $PathrepoSmtp){
		    $package="$PathrepoSmtp$smtp";
		    $IP_SMTP_RH=`grep 'relayhost = ' $package 2> /dev/null | sed "s+relayhost = ++g"`;
		    $IP_SMTP_FB=`grep 'fallback_relay' $package 2> /dev/null | grep -v '#' | sed "s+fallback_relay = ++g"`;
		    $IP_SMTP_RH=~ s/\n/ /g;
		    $IP_SMTP_FB=~ s/\n/ /g;
		}
}

sub get_conf_nrpe{
	
		#echo "----------------------------------conf_NRPE : " >> ../$HOME/tmp.txt
	    if (-d $Pathnrpe){
		    $package="$Pathnrpe$nrpe";
		    $IP_NRPE=`grep 'server_address=' $package | sed "s+server_address=++g"`;
		    $IP_NRPE=~ s/\n/ /g;
	    }
}


###################################################### MODIF CONF FILE ################################################################################
sub modif_conf_file {
	my ($FILES)= @_;
	print "Voulez vous modifier un élément de la configuration ? (yes/no) : ";
	if (<STDIN> =~/y/){	
		for ($FILES){
			if(/NRPE/){$FILES="/etc/nagios/nrpe.cfg";}
			elsif(/SNMP/){$FILES="/etc/snmp/snmpd.conf";}
			elsif(/SMTP/){$FILES="/etc/postfix/main.cf";}
			elsif(/LDAP/){	
					if (-f "/etc/libnss-ldap.conf"){
							$FILES="/etc/libnss-ldap.conf";
					}else {$FILES="/etc/ldap/ldap.conf";}}
			elsif(/NTP/){$FILES="/etc/ntp.conf";}
			elsif(/DNS/){$FILES="/etc/resolv.conf";}
			elsif(/puppet/){$FILES="/etc/puppet/puppet.conf";}
		}
		if (-d $FILES ){
			print "###########################################   CONTENU DE $FILES   ###########################################\n";
			my $F=`cat $FILES | grep -v '#' | grep [0-9]`;
			print $F."\n";
			print "Parametre à modifier : \n";
			my $old_IP = <STDIN>;
			$old_IP=~ s/\n//g;
			print "Nouveau parametre à implementer : \n";
			my $new_IP = <STDIN>;
			$new_IP=~ s/\n//g;
			CALDATE;
			my $FILE2=`echo "$FILES" | awk -F '/' '{ print \$NF }'`;
			`cp "$FILES" "/tmp/$FILE2-$datestamp-$timestamp"`;
			`sed -i "s+$old_IP+$new_IP+g" $FILES`;
	
		}else{ 	print "Le fichier $FILES  n'est pas present sur cet equipement ";}
		#choice;
	}
			
}

sub insert_conf_file {
	my $FILES;
	print "Voulez vous ajouter un élément dans le fichier de configuration ? (yes/no) : ";
	if (<STDIN> =~/yes/){	
		print "Quel fichier ?\n";
		print "-	NRPE\n";
		print "-	SNMP\n";
		print "-	SMTP\n";
		print "-	LDAP\n";
		print "-	NTP\n";
		print "-	DNS\n";
		print "-	puppet\n";

			if(<STDIN>=~/NRPE/){$FILES="/etc/nagios/nrpe.cfg";}
			elsif(<STDIN>=~/SNMP/){$FILES="/etc/snmp/snmpd.conf";}
			elsif(<STDIN>=~/SMTP/){$FILES="/etc/postfix/main.cf";}
			elsif(<STDIN>=~/LDAP/){	
					if (-f "/etc/libnss-ldap.conf"){
							$FILES="/etc/libnss-ldap.conf";
					}else {$FILES="/etc/ldap/ldap.conf";}}
			elsif(<STDIN>=~/NTP/){$FILES="/etc/ntp.conf";}
			elsif(<STDIN>=~/DNS/){$FILES="/etc/resolv.conf";}
			elsif(<STDIN>=~/puppet/){$FILES="/etc/puppet/puppet.conf";}

		if (-e $FILES ){
			echo_color_blue("###########################################   CONTENU DE $FILES   ###########################################");
			my $F=`cat $FILES | grep -v '#' | grep [0-9]`;
			$F=~ s/[0-9]//g;
			$F=~ s/\.//g;
			print $F."\n";
			print "Parametre à ajouter : \n";
			my $new_IP = <STDIN>;
			$new_IP=~ s/\n//g;
			CALDATE;	
			#
			#my $FILE2=`echo "$FILES" | awk -F '/' '{ print \$NF }'`;
			#`cp "$FILES" "/tmp/$FILE2-$datestamp-$timestamp"`;
			#`sed -i "s+$old_IP+$new_IP+g" $FILES`;
	
		}else{ 	print "Le fichier $FILES  n'est pas present sur cet equipement \n";}
		#choice;
	}
			
}

sub insert_path{
	my $FILES="/etc/puppet/puppet.conf";
	if (-e $FILES){
			echo_color_blue("###########################################   CONTENU DE $FILES   ###########################################");
			my $F=`grep 'classfile' $FILES | sed 's+ ++g'`;
			print $F."\n";
			print "Parametre à modifier : \n";
			my $old_IP = <STDIN>;
			$old_IP=~ s/\n//g;
			print "Nouveau parametre à implementer : \n";
			my $new_IP = <STDIN>;
			$new_IP=~ s/\n//g;
			CALDATE;
			my $FILE2=`echo "$FILES" | awk -F '/' '{ print \$NF }'`;
			`cp "$FILES" "/tmp/$FILE2-$datestamp-$timestamp"`;
			`sed -i "s+$old_IP+$new_IP+g" $FILES`;
	
		}else{ 	print "Le fichier $FILES  n'est pas present sur cet equipement \n";}
}

###################################################### CONF PUPPET ################################################################################ 

sub check_cmd_puppetd {
#test la precense de la cmd puppetd

	print "\n=> TEST PRESENCE PUPPETD\n";
	my $testpresencepuppetd=`which puppetd 2> /dev/null | grep -qiE 'puppetd'`;
	if ($testpresencepuppetd){
		$GLOBAL_CONF="$GLOBAL_CONF OK";
		echo_color_green("presence de la commande \"puppetd\"");

	}else{
		$GLOBAL_CONF="$GLOBAL_CONF NOK";
		echo_color_red("absence de la commande \"puppetd\"");
	}
	
}

sub conf_puppet {
	
	print "\n=> CONF PUPPET\n";
	my $puppet_run;
	my $puppet_class;
	my $puppet_server;
	my $FILE="/etc/puppet/puppet.conf";
	if (-e $FILE ){
		$puppet_run=`grep 'rundir' $FILE | sed "s+rundir=++g" | sed 's+ ++g'`;
		$puppet_class=`grep 'classfile' $FILE | sed "s+classfile =++g" | sed 's+ ++g'`;
		$puppet_server=`grep 'server' $FILE | sed "s+server=++g" | sed 's+ ++g'`;
		unless($puppet_server){
			$puppet_server=`grep 'SERVER' $FILE | sed "s+SERVER :++g" | sed 's+ ++g'`;
		}
	}else{
		echo_color_red("$FILE inexistant");
		$GLOBAL_CONF="$GLOBAL_CONF NOK";
	}
	
	if($puppet_run){
		$puppet_run=~ s/\n//g;
		&echo_color_yellow("RUNDIR : $puppet_run");
	}
	
	if ($puppet_class){
		$puppet_class=~ s/\n//g;
		echo_color_yellow("CLASSFILE : $puppet_class");
	}
	
	if($puppet_server){
		$puppet_server=~ s/\n//g;
		echo_color_yellow("SERVER : $puppet_server");
		my $PING=`ping -W 10 -c 3 $puppet_server`; 
		if($PING){
			my $testPuppet=`echo "$PING" | grep -qiE '0 received, 100% packet loss| Network is unreachable'`;
			if ($testPuppet){
			    &echo_color_red("ping $puppet_server fail");
			    $GLOBAL_CONF="$GLOBAL_CONF NOK";
			}else{
			    &echo_color_green("ping $puppet_server ok");
			    $GLOBAL_CONF="$GLOBAL_CONF OK";
			}##fin de boucle if ping SVCC
		}else{
		    &echo_color_red("ping $puppet_server fail");
		    $GLOBAL_CONF="$GLOBAL_CONF NOK";
		}
	}

}

sub delta_conf_puppet {
#test du delta entre la conf puppet distante et la local
#check_cmd_puppetd
#conf_puppet

print "\n=> DELTA PUPPET\n";
my $puppet_run=`grep 'rundir' "/etc/puppet/puppet.conf" | sed "s+rundir=++g" | sed 's+ ++g'`;
if (-d "/etc/puppet/puppet.conf"){
	if (" ! -d /etc/puppet/puppetd.conf"){
	
		if ("$puppet_run" eq "/var/run/puppet" && `ls -A "$puppet_run" | wc -c` == 0 ){
			`puppetd -tv --noop |grep "should be" 2> /dev/null`;
			my $testdeltapuppet=`echo $?`;
			if ("$testdeltapuppet" != 0){
				
				&echo_color_green("pas de delta");
			
			}else {
				&echo_color_red("difference entre la conf puppet distante et la conf puppet local");
			} 
				
			#rm /tmp/tmp.txt
			#exit 0
		}else{&echo_color_red("/var/run/puppet non existant ou non vide");}
	}else{&echo_color_red("/etc/puppet/puppetd.conf existant");	}	
	}else{&echo_color_red("/etc/puppet/puppet.conf inexistant");}
}

###################################################  TEST SERVICE  #########################################################################"

sub serv_smtp{
	print "\n=> SMTP\n";
	&get_conf_smtp;
	my $FILE="/etc/postfix/main.cf";
	if(-e $FILE){
		if ("$IP_SMTP_RH" ne ""){
			foreach my $SMTP_IP (split(' ',$IP_SMTP_RH)){
				#if("$IP_SMTP_RH" eq " " && "$IP_SMTP_RH" eq ";"){
					$SMTP_IP=~s/\n//g;
					my $SMTP=`nc -z -w 1 -q 1 $SMTP_IP 25 2> /dev/null ; if [ $? = 0 ]; then echo "OK"; else echo "NOK"; fi;`;
					$GLOBAL_VAR="$GLOBAL_VAR$SMTP";
					unless ($SMTP=~/NOK/){
						&echo_color_green("$SMTP_IP Service SMTP relayHost (test port 25 + telnet)");
					}else{
						&echo_color_red ("$SMTP_IP Service SMTP relayHost (test port 25 + telnet)");
					}
				#}
			}
		}else{ 
			&echo_color_red("absence de conf SMTP (/etc/postfix/main.cf)");
			$GLOBAL_VAR=${GLOBAL_VAR}."NOK"
		}
		if ("$IP_SMTP_FB" ne ""){
			foreach my $SMTP_IP (split(' ',$IP_SMTP_FB)){
				#if("$IP_SMTP_RH" eq " " && "$IP_SMTP_RH" eq ";"){
					$SMTP_IP=~s/\n//g;
					my $SMTP=`nc -z -w 1 -q 1 $SMTP_IP 25 2> /dev/null ; if [ $? = 0 ]; then echo "OK"; else echo "NOK"; fi;`;
					$GLOBAL_VAR="$GLOBAL_VAR$SMTP";
					unless ($SMTP=~/NOK/){
						&echo_color_green("$SMTP_IP Service SMTP FallBack (test port 25 + telnet)");
					}else{
						&echo_color_red ("$SMTP_IP Service SMTP FallBack (test port 25 + telnet)");
					}
				#}
			}
		}else{ 
			&echo_color_red("absence de conf SMTP (/etc/postfix/main.cf)");
			$GLOBAL_VAR=${GLOBAL_VAR}."NOK"
		}
	}else{ 
		&echo_color_red ("absence de conf SMTP (/etc/postfix/main.cf)");
		$GLOBAL_VAR=${GLOBAL_VAR}."NOK"
	}
}


sub serv_dns{
	&get_conf_dns;
	if ("$IP_DNS" ne ""){
		print "\n=> DNS\n";
		my $FILE=$dns;
		if(-e $FILE){
			foreach my $DNS_IP (split(' ',$IP_DNS)){
					$DNS_IP=~s/\n//g;
					my $DNS =`nc -z -w 1 -q 1 $DNS_IP 53 2> /dev/null ; if [ $? = 0 ]; then echo "OK"; else echo "NOK"; fi;`;
					$GLOBAL_VAR="$GLOBAL_VAR$DNS";
					unless ($DNS=~/NOK/){
						&echo_color_green("$DNS_IP (test port 53)");
					}else{
						&echo_color_red ("$DNS_IP (test port 53)");
					}
					
					#test de concordance avec NSLOOKUP 
					my $ADDR=`nslookup \$HOSTNAME "$DNS_IP"`;
					my $rep_IP=`nslookup \$HOSTNAME "$DNS_IP" | grep 'Address' | sed 's+Address:++g' | cut -d '#' -f1`;
					$rep_IP=~ s/[\t\s]//g;
					my $DNS_IP2=`nslookup \$HOSTNAME "$DNS_IP" | grep 'Server:' | sed 's+Server:++g'`;
					$DNS_IP2=~ s/[\t\s]//g;
					$DNS_IP=~ s/[\t\s]//g;
					if($DNS_IP2=~/$DNS_IP/){
	    				my $HOST=`nslookup "$rep_IP" | grep -i "name" | cut -d '=' -f2 `;
	    				$HOST=~s/\n//g;
	    				my $short_HOST1=`\$HOSTNAME | cut -d '-' -f1`;
	    				my $short_HOST2=`\$HOSTNAME | cut -d '-' -f2`;
	    				my $HOST3="$short_HOST1-$short_HOST2";
	    				$HOST3=~s/\n//g;
	    				#my $testnslookup=`echo $HOST | grep '$HOST3'`;
					    if($HOST =~/$HOST3/){
						      &echo_color_green("$DNS_IP (comparaison NAME/IP puis IP/NAME)\n");
						      $GLOBAL_VAR=${GLOBAL_VAR}."OK";

					    }else{
						      &echo_color_red("$DNS_IP (comparaison NAME/IP puis IP/NAME)\n");
						      $GLOBAL_VAR=${GLOBAL_VAR}."NOK";
					    }
					}else{					    
					    &echo_color_red("$DNS_IP (comparaison NAME/IP puis IP/NAME)\n");
					    $GLOBAL_VAR=${GLOBAL_VAR}."NOK"
					}
			}
		}else{ 
			&echo_color_red("absence de conf DNS (${dns})\n");
			$GLOBAL_VAR=${GLOBAL_VAR}."NOK"
		}
	}else{ 
		&echo_color_red ("absence de conf DNS (${dns})\n");
		$GLOBAL_VAR=${GLOBAL_VAR}."NOK"
	}
}

sub dig {
		#test la presence de 'dig'
		my $testpresencedig=`which dig | grep -E 'dig' 2> /dev/null`;
		if($testpresencedig){
			# test DNS #
			my $ADDR=`dig +short www.sfr.fr | grep -E [0-9]`;
			$ADDR=~s/127\.0\.0\.1//g;
			if($ADDR){
			    $GLOBAL_VAR=${GLOBAL_VAR}."OK";
			    &echo_color_green("Resolution de \"www.sfr.fr\" (=> dig)");
			    #print "\n";
			}else{
			    &echo_color_red("Resolution de \"www.sfr.fr\" (=> dig)");
			    $GLOBAL_VAR=${GLOBAL_VAR}."NOK";
			    #print "\n";
			} ##fin de boucle if testdns
		}else{
			my $ADDR=`nslookup  www.sfr.fr | grep -E [0-9]`;
			$ADDR=~s/127\.0\.0\.1//g;
			if($ADDR){
			$GLOBAL_VAR=${GLOBAL_VAR}."OK";
			&echo_color_green("Resolution de \"www.sfr.fr\" (=> nslookup)");
			}else{
			    &echo_color_red("Resolution de \"www.sfr.fr\" (=> nslookup)");
			    $GLOBAL_VAR=${GLOBAL_VAR}."NOK";
			} ##fin de boucle if testdns
		} ##fin de boucle testpresencedig
}


sub serv_ntp{
	&get_conf_ntp;
	if ($IP_NTP){
		print "\n=> NTP\n";

		my $FILE=$ntp;
		if(-e $FILE){
			foreach my $NTP_IP (split(' ',$IP_NTP)){
				my $NTP=`nc -zu -w 1 -q 1 $NTP_IP 123 2> /dev/null ; if [ $? = 0 ]; then echo "OK"; else echo "NOK"; fi;` ;
				$GLOBAL_VAR="$GLOBAL_VAR$NTP";
				unless ($NTP=~/NOK/){
						&echo_color_green("$NTP_IP (test port 123)");
				}else{
						&echo_color_red ("$NTP_IP (test port 123)");
				}
				my $testpresencedig=`which ntpdate | grep -E 'ntpdate' 2> /dev/null`;
				if($testpresencedig){
					`ntpdate -q 127.0.0.1 2> /dev/null`;
					my $testntp=`echo $?`;
					if ("$testntp" == 0){
							&echo_color_green("$NTP_IP (ntpdate) \n");
					}else{
							&echo_color_red ("$NTP_IP (ntpdate) \n");
					}
				}else{&echo_color_red ("$NTP_IP (abcense de la cmd \'ntpdate\ \n')");}
			}
		}

	}
}

sub serv_ldap{
	&get_conf_ldap;
	if ($IP_LDAP){
		print "\n=> LDAP\n";

			foreach my $LDAP_IP (split(' ',$IP_LDAP)){
				my $LDAP=`nc -z -w 1 -q 1 $LDAP_IP 389 2> /dev/null ; if [ $? = 0 ]; then echo "OK"; else echo "NOK"; fi;`;
				$GLOBAL_VAR="$GLOBAL_VAR$LDAP";
				unless ($LDAP=~/NOK/){
						&echo_color_green("$LDAP_IP (test port 389)");
				}else{
						&echo_color_red ("$LDAP_IP (test port 389)");
				}
				if(`getent passwd support_tech`){
						&echo_color_green("$LDAP_IP presence du compte \"support_tech\"");
						$GLOBAL_VAR=${GLOBAL_VAR}."OK";
				}else{
						&echo_color_red ("$LDAP_IP absence du compte \"support_tech\"");
						$GLOBAL_VAR=${GLOBAL_VAR}."NOK";
						
				}
			}
		}

}


#####################################################  Route #################################################################################
# Liste des routes
sub Route{

	my $ROUTES=`netstat -rn | tail -n+3 | sort -k8`;
	
	
	if ($ROUTES){
		print "=> ROUTES\n";
		print $ROUTES."\n";
		$GLOBAL_VAR=${GLOBAL_VAR}."OK";
	}else{
		&echo_color_red("PAS DE ROUTES");
		$GLOBAL_VAR=${GLOBAL_VAR}."NOK";
	}

}


##################################################################################################################################################


sub get_all_param{
	check_os;
	get_conf_ldap;
	get_conf_ntp;
	get_conf_dns;
	get_conf_snmp;
	get_conf_smtp;
	get_conf_nrpe;
	&conf_puppet;
}

sub print_result {
	
check_os;
get_conf_ldap;
get_conf_ntp;
get_conf_dns;
get_conf_snmp;
get_conf_smtp;
get_conf_nrpe;
echo_color_yellow("------------------------------------------------------------------------------");
echo_color_yellow("-----------------------------------RESULT-------------------------------------");
echo_color_yellow("------------------------------------------------------------------------------");
print "$IP_LDAP ; $IP_NTP ; $IP_DNS ; $IP_SNMP ; $IP_SMTP_RH ; $IP_SMTP_FB ; $IP_NRPE ;\n";
exit 0

}

sub parametre{
	echo_color_blue ("#############################################################################################################################");
	echo_color_blue ("###############################          TEST PARAMETRE(S)       ############################################################");
	echo_color_blue ("#############################################################################################################################");
	#&insert_conf_file;
}

sub conf{

	echo_color_blue ("#############################################################################################################################");
	echo_color_blue ("##############################           TEST CONFIGURATIONS      ###########################################################");
	echo_color_blue ("#############################################################################################################################");
	&check_cmd_puppetd;
	&conf_puppet;
	print "\n";
	if($GLOBAL_CONF=~/N/){
		echo_color_red("CONFIGURATION GLOBAL");
	}else{
		echo_color_green("CONFIGURATION GLOBAL"); 
	}

}


sub test_conf {
	
	my ($IP,$conf,$opt) = @_;
	my $LOCAL_CONF="";
	my $IP_split;
	foreach $IP_split (split(' ',$IP)){
		if("$opt" eq "$IP_split"){
			$LOCAL_CONF="$LOCAL_CONF Y";
		}else{
			$LOCAL_CONF="$LOCAL_CONF N";
		}		
	}
	unless($LOCAL_CONF=~/Y/){
		echo_color_red("$conf $opt");
		$GLOBAL_CONF="$GLOBAL_CONF NOK";
		&modif_conf_file("$conf");
	}else{
		echo_color_green("$conf $opt");
		$GLOBAL_CONF="$GLOBAL_CONF OK";
	}
}


sub serv{

	echo_color_blue ("#############################################################################################################################");
	echo_color_blue ("####################################           TEST SERVICES      ###########################################################");
	echo_color_blue ("#############################################################################################################################");
	print "\n";
	serv_smtp;
	print "\n";
	&serv_dns;
	#print "\n";
	&dig;
	print "\n";
	&serv_ntp;
	print "\n";
	&serv_ldap;
	print "\n";
	&Route;
	
	if($GLOBAL_VAR=~/N/){
		echo_color_red("SERVICE GLOBAL"); 
	}else{
		echo_color_green("SERVICE GLOBAL"); 
	}

}

&conf;
&serv;
