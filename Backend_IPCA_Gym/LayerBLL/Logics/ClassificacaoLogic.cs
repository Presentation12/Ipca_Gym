using LayerBLL.Utils;
using LayerBOL.Models;
using Microsoft.AspNetCore.Mvc;
using LayerDAL;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Numerics;
using System.Text;
using System.Threading.Tasks;


namespace LayerBLL.Logics
{
    public class ClassificacaoLogic
    { 
        public static async Task<Response> GetClassificacoesLogic(string sqlDataSource)
    {
        Response response = new Response();
        List<Classificacao> classificacaoList = await LayerDAL.ClassificacaoService.GetClassificacoesService(sqlDataSource);
        if (classificacaoList.Count != 0)
        {
            response.StatusCode = StatusCodes.SUCCESS;
            response.Message = "Sucesso na obtenção dos dados";
            response.Data = new JsonResult(classificacaoList);
        }
            return response;
        }
    }
}
