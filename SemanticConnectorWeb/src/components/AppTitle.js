import { Typography} from "@mui/material";
// import { makeStyles } from "@mui/styles";

// const useStyles = makeStyles(theme => {

// });
export default function AppTitle() {
    // const classes = useStyles();

    return (
        <div className="input-bar">
            <Typography variant="h4" color='primary' fontWeight='bold'>
                Semantische Annotation und Verknüpfung duch den Nutzer
            </Typography>
            <Typography variant="subtitle1" color='primary'>
                (Abfrage und Speicherung über HTTP-Request an GraphDB mit SPARQL)
            </Typography>
        </div>
    )
}