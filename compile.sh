#!/usr/bin/env bash
###################################  CONSTANTES  ###################################
DATE=$(date +%Y-%m-%d)
JAVA_HOME=/opt/jdk1.8.0_191
JAVA_HOME=/home/dacopan/.sdkman/candidates/java/8.0.332-tem
echo $JAVA_HOME
LINE="--------------------------------------------------------------------------"

# COLORES
LBLUE="\033[1;34m"  #1
LGREEN="\033[1;32m" #2
LRED="\033[1;31m"   #3
LCYAN="\033[1;36m"  #4
NC="\033[0m"        #No Color



###################################  FUNCIONES  ###################################
# Descriccion de los parametros del script
function usage {
    echo
    echo "Uso: deploy.sh [-ciu {server, web, all}]"
    echo "Compila los proyectos, crea la imagen docker y sube al registry de; gitlab"
    echo "  -c {server, web, all}       compila el proyecto"
    echo "  -i {server, web, all}       crea la imagen docker"
    echo "  -u {server, web, all}       sube la imagen al gitlab registry"
    exit 1
}

# Muestra mensajes con colores
function text_color {
    local text=$2
    if [[ $1 -eq 1 ]]; then
        text="${LBLUE}$2${NC}"
    elif [[ $1 -eq 2 ]]; then
        text="${LGREEN}$2${NC}"
    elif [[ $1 -eq 3 ]]; then
        text="${LRED}$2${NC}"
    elif [[ $1 -eq 4 ]]; then
        text="${LCYAN}$2${NC}"
    fi
    echo "$text"
}

function cecho {
    if [[ $# -gt 0 ]] && [[ $(( $# % 2 )) -eq 0 ]]; then
        local text=""
        while (( "$#" )); do
            num=$1
            shift
            local color=$(text_color num "$1")
            text="${text}${color}"
            shift
        done
        echo -e "${text}"
    fi
}

function check_success {
    if [[ $1 -eq 0 ]]; then
        cecho 2 "OK"
    else
        cecho 3 "ERROR"
        exit 1
    fi
}

#####################################  MAIN  #####################################
function compile_web {
    cecho 1 "Eliminado directorio: " 2 "./web/build"
    rm -rf ./web/build
    check_success $?
    cecho 1 "Ejecutando comando: " 2 "npm run build"
    cd web && npm run build && cd ..
    check_success $?
}

function compile_server {
    cecho 1 "Eliminado directorio: " 2 "./web/build"
    rm -rf ./build
    check_success $?
    cecho 1 "Ejecutando comando: " 2 "gradlew clean jar"
    bash ./gradlew clean jar
    check_success $?
}

function upload_server {
#    b2 authorize-account 000cd3f731a95fd0000000006 K000DFt5VaZe1rlvOZzIkgjw+0BE0vg
#    b2 upload-file dacopan build/libs/dremio-db2-plugin-1.0.jar artifacts/dremio-db2-plugin-1.0.jar
#    check_success $?

    #cp -rf build/libs/dremio-db2-plugin-1.0.jar /media/enki/ba8fff7e-084d-4b8a-bf16-82b14112fdb8/downloads/dremios/dremio-community-4.6.1-202007220122450047-62e084d0/jars/dremio-db2-plugin-1.0.jar
    cp -rf build/libs/dremio-db2-plugin-1.2.jar /media/enki/ba8fff7e-084d-4b8a-bf16-82b14112fdb8/downloads/dremios/dremio-community-4.1.3-202001022113020736-53142377/jars/dremio-db2-plugin-1.0.jar
   #ls
}

function main {
    OPTS=":ciu"
    while getopts "${OPTS}" opt; do
        case ${opt} in
            \?)
                cecho 3 "Parámetro no válido ($1)" >&2
                usage
            ;;
        esac
    done
    eval "FIRSTARG=\${${OPTIND}}"
    if [[ -z "${FIRSTARG}" ]]; then
        cecho 3 "Debe especificar el nombre del proyecto"
        usage
    fi
    OPTIND=1
    FIRSTARG=${FIRSTARG,,}
    while getopts "${OPTS}" opt; do
        case ${opt} in
            c)
                cecho 1 ${LINE}
                cecho 2 "               C O M P I L A N D O   P R O Y E C T O"
                cecho 1 ${LINE}
                compile_server
            ;;
            u)
                cecho 1 $LINE
                cecho 2 "       S U B I E N D O   I M A G E N E S   A L   J F R O G"
                cecho 1 $LINE
                upload_server
            ;;
        esac
    done

    if [[ $OPTIND -eq 2 ]]; then
        shift $OPTIND
    else
        cecho 3 "Parámetro no válido ($1)" >&2
        usage
    fi

    if [[ $# -ge 2 ]]; then
        main $@
    fi
    exit 0
}
main $@