import React from 'react';
import { Button, Grid, Paper } from '@mui/material';
import DropdownButton from './DropdownButton';
import { sendJson } from '../backend_conn';
import initValues from '../StaticValues';
import useStyles from '../Styles';

export default function DatacatPanel(props) {
    const classes = useStyles();
    const repository = props.repository;

    const triple = {
        instance: "",
        className: "",
    }

    const [elValue, setElValue] = React.useState(initValues);
    function handleCallback(id, item) {
        const newElValue = elValue.map((i, eid) => eid === id ? item : i);
        setElValue(newElValue);
    }

    function sendTriples(triple1) {
        const response = async () => {
            const res = await sendJson("POST", "/insertclassdefinition/repo/" + repository, triple1);
            console.log(res);
        }
        response();
    }

    return (
        <div className={classes.maxWidth}>
            <Grid container spacing={1} columns={8} className={classes.root}>
                <Grid item sm={2} >
                    <Paper className={classes.text}>
                        Semantische Annotation von TT Objekten mit Klassen der TT Ontologie
                    </Paper>
                </Grid>
                <Grid item sm={1}>
                    <DropdownButton id={3} elValue={elValue[3]} initValue="" repository={repository} value={elValue} onChange={handleCallback} />
                </Grid>
                <Grid item sm={1}>
                    <Paper className={classes.text} style={{ lineHeight: "50px" }}>
                        rdf:type
                    </Paper>
                </Grid>
                <Grid item sm={1}>
                </Grid>
                <Grid item sm={1}>
                    <DropdownButton id={4} elValue={elValue[4]} initValue="" repository={repository} value={elValue} onChange={handleCallback} />
                </Grid>
                <Grid item sm={1}>
                </Grid>
                <Grid item sm={1}>
                    <Button className={classes.button} onClick={() => {
                        triple.instance = elValue[3].value;
                        triple.className = elValue[4].value;
                        sendTriples(triple);
                    }}>Save</Button>
                </Grid>
            </Grid>
        </div>
    )
}