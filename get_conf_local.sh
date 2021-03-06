#!/bin/bash

#boucle pour la propagation
#for i in `cat /tmp/list` ; do echo $i ;scp ~/get_conf_distant.sh $1@$i:/tmp ; done


# Reset
Color_Off='\e[0m'       # Text Reset

# Regular Colors
Black='\e[0;30m'        # Black
Red='\e[0;31m'          # Red
Green='\e[0;32m'        # Green
Yellow='\e[0;33m'       # Yellow
Blue='\e[0;34m'         # Blue
Purple='\e[0;35m'       # Purple
Cyan='\e[0;36m'         # Cyan
White='\e[0;37m'        # White

# Bold
BBlack='\e[1;30m'       # Black
BRed='\e[1;31m'         # Red
BGreen='\e[1;32m'       # Green
BYellow='\e[1;33m'      # Yellow
BBlue='\e[1;34m'        # Blue
BPurple='\e[1;35m'      # Purple
BCyan='\e[1;36m'        # Cyan
BWhite='\e[1;37m'       # White

function color_echo {
    echo -en "$1$2${Color_Off}"
}
function init_substep {
        ERR_STEP_FLAG=0
        COMMENT=""
}
function init_step {
        echo
        echo "> $STEP" | tr a-z A-Z
        init_substep
}

function step_status {
        echo -en "["
        if [ $ERR_STEP_FLAG -eq 0 ]; then
                color_echo $Green "OK"
        else 
                color_echo $Red "NOK"
        fi  
        echo -en "] \t$STEP"
        if [ "$1" != "" ]; then 
                echo -ne " - $1"
        fi  
        echo 
}

#####################################################################################################################

#recupére les variables passé en paramétre > user et @IP
user=$1
ip=$2
shift 2
arg=$*

#echo "cmd : sudo /bin/bash /tmp/get_conf_distant.sh $arg"


###################################################"COMMON TOOLS"#################################################
# some functions
help()
{
    cat <<HELP

usage: get_conf_local {-h  -v } [USER] [@IP_EQUIPEMENT] 
 -h => Help
 -v => Version

appel le script d'audit distant sur l'equipement fourni : "get_conf_distant {-c -e -h -r -v -d [@DNS] -l [@LDAP] -m [@SMTP] -n [@NTP] -p [@NRPE] -s [@SNMP]}"
Possible de passer les parametres au script distant, ex :
usage: get_conf_distant {-c -e -h -r -v -d [@DNS] -l [@LDAP] -m [@SMTP] -n [@NTP] -p [@NRPE] -s [@SNMP]}
recupere, test et affiche et modifie au besoin les configurations des services presents sur l'equipements

 -c => delta_Conf_puppet
 -d => Dns
 -e => changeE_puppet_server_path
 -h => Help
 -l => Ldap
 -m => sMtp
 -n => Ntp
 -p => nrPe
 -r => print Results (like version 1.01)
 -s => Snmp
 -v => Version

HELP
    exit 0
}


function version {
   echo -e  "

$0  version 2.00 - May 6th, 2013
2013 Jason LELORRAIN
This program is free software; you can redistribute it and/or modify it under the same terms as shell itself. 

"
exit 0
}

error()
{
    # display error
    echo -e "\n[ERROR $1]\t$2\n\n----> Please run 'get_conf_local -h' for the help\n" 2> stderr
    exit 1
}


if [ `echo $arg | grep [a-z] | wc -l` -eq 0 ] || [ `echo $ip | grep [0-9] | wc -l` -eq 0 ] || [ `echo $user | grep [a-z] | wc -l` -eq 0 ]; then
	while getopts ":hv" OPTION
	do
	    case $OPTION in
	    h) help;;
	    v) version;;
	    :) error 1 "Option '-$OPTION' needs an argument."
	       exit 1;;
	    *) error 2 "Unrecognized option '-$OPTION'."
	       exit 1;;
	    esac
	done
fi


############################################## PING SVCC #######################################################################
STEP="PING SVCC ($ip)"
init_step

ADDR=$(ping -W 10 -c 3 $ip)
testDNS=$(echo "$ADDR" | grep -qiE '0 received, 100% packet loss| Network is unreachable' && echo "KO" || echo "OK")
#echo $testDNS
if [ "$testDNS" = "KO" ]; then
    init_substep
    ERR_STEP_FLAG=1  
    step_status "ping fail"
    echo
else
    init_substep
    ERR_STEP_FLAG=0
    step_status "ping ok"
    echo
fi ##fin de boucle if ping SVCC


###############################################################################################################################
#connexion
echo
ssh -t $user@$ip sudo /usr/bin/perl /tmp/get_conf_distant.pl $arg
#./get_conf_distant.sh
exit 0




